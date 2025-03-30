package net.programmer.igoodie.streamspawn.javascript.classes;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.tsl.util.Pair;
import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

/**
 * Carbon-copy of ScriptableObject::defineClass,
 * because I'm way too lazy implementing mixins lol
 */
public class ClassDefiner {

    private static boolean sawSecurityException;

    public static <T extends Scriptable> Pair<String, BaseFunction> defineClass(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance) {
        try {
            BaseFunction ctor = ClassDefiner.buildClassCtor(scope, clazz, sealed, mapInheritance);
            String name = getClassPrototype(ctor).getClassName();
            ScriptableObject.defineProperty(scope, name, ctor, ScriptableObject.DONTENUM);
            return new Pair<>(name, ctor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Enum<T>> Pair<String, NativeObject> defineEnum(ScriptableObject scope, String name,Class<T> clazz) {
        NativeObject enumObject = new NativeObject();

        for (T enumConstant : clazz.getEnumConstants()) {
            ScriptableObject.defineProperty(enumObject, enumConstant.name(), enumConstant, ScriptableObject.EMPTY);
        }

        ScriptableObject.putConstProperty(scope, name, enumObject);

        return new Pair<>(name, enumObject);
    }

    protected static <T extends Scriptable> BaseFunction buildClassCtor(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Context cx = JavascriptEngine.CONTEXT.get();

        Method[] methods = getMethodList(clazz);
        for (Method method : methods) {
            if (!method.getName().equals("init")) continue;
            Class<?>[] parmTypes = method.getParameterTypes();
            if (parmTypes.length == 3
                    && parmTypes[0] == ScriptRuntime.ContextClass
                    && parmTypes[1] == ScriptRuntime.ScriptableClass
                    && parmTypes[2] == Boolean.TYPE
                    && Modifier.isStatic(method.getModifiers())) {
                Object[] args = {cx, scope, sealed ? Boolean.TRUE : Boolean.FALSE};
                method.invoke(null, args);
                return null;
            }
            if (parmTypes.length == 1
                    && parmTypes[0] == ScriptRuntime.ScriptableClass
                    && Modifier.isStatic(method.getModifiers())) {
                Object[] args = {scope};
                method.invoke(null, args);
                return null;
            }
        }

        // If we got here, there isn't an "init" method with the right
        // parameter types.

        Constructor<?>[] ctors = clazz.getConstructors();
        Constructor<?> protoCtor = null;
        for (Constructor<?> constructor : ctors) {
            if (constructor.getParameterTypes().length == 0) {
                protoCtor = constructor;
                break;
            }
        }
        if (protoCtor == null) {
            throw reportRuntimeErrorById("msg.zero.arg.ctor", clazz.getName());
        }

        Scriptable proto = (Scriptable) protoCtor.newInstance(ScriptRuntime.emptyArgs);
        String className = proto.getClassName();

        // check for possible redefinition
        Object existing = ScriptableObject.getProperty(getTopLevelScope(scope), className);
        if (existing instanceof BaseFunction) {
            Object existingProto = ((BaseFunction) existing).getPrototypeProperty();
            if (existingProto != null && clazz.equals(existingProto.getClass())) {
                return (BaseFunction) existing;
            }
        }

        // Set the prototype's prototype, trying to map Java inheritance to JS
        // prototype-based inheritance if requested to do so.
        Scriptable superProto = null;
        if (mapInheritance) {
            Class<? super T> superClass = clazz.getSuperclass();
            if (ScriptRuntime.ScriptableClass.isAssignableFrom(superClass)
                    && !Modifier.isAbstract(superClass.getModifiers())) {
                Class<? extends Scriptable> superScriptable = extendsScriptable(superClass);
                Pair<String, BaseFunction> result = ClassDefiner.defineClass(scope, superScriptable, sealed, mapInheritance);
                String name = result.getLeft();
                if (name != null) {
                    superProto = ScriptableObject.getClassPrototype(scope, name);
                }
            }
        }
        if (superProto == null) {
            superProto = ScriptableObject.getObjectPrototype(scope);
        }
        proto.setPrototype(superProto);

        // Find out whether there are any methods that begin with
        // "js". If so, then only methods that begin with special
        // prefixes will be defined as JavaScript entities.
        final String functionPrefix = "jsFunction_";
        final String staticFunctionPrefix = "jsStaticFunction_";
        final String getterPrefix = "jsGet_";
        final String setterPrefix = "jsSet_";
        final String ctorName = "jsConstructor";

        Member ctorMember = findAnnotatedMember(methods, JSConstructor.class);
        if (ctorMember == null) {
            ctorMember = findAnnotatedMember(ctors, JSConstructor.class);
        }
        if (ctorMember == null) {
            ctorMember = findSingleMethod(methods, ctorName);
        }
        if (ctorMember == null) {
            if (ctors.length == 1) {
                ctorMember = ctors[0];
            } else if (ctors.length == 2) {
                if (ctors[0].getParameterTypes().length == 0) ctorMember = ctors[1];
                else if (ctors[1].getParameterTypes().length == 0) ctorMember = ctors[0];
            }
            if (ctorMember == null) {
                throw reportRuntimeErrorById("msg.ctor.multiple.parms", clazz.getName());
            }
        }

        FunctionObject ctor = new FunctionObject(className, ctorMember, scope);
        // TODO:
//        if (ctor.isVarArgsMethod()) {
//            throw reportRuntimeErrorById("msg.varargs.ctor", ctorMember.getName());
//        }
        initAsConstructor(
                ctor,
                scope,
                proto,
                ScriptableObject.DONTENUM | ScriptableObject.PERMANENT | ScriptableObject.READONLY);

        Method finishInit = null;
        HashSet<String> staticNames = new HashSet<>(), instanceNames = new HashSet<>();
        for (Method method : methods) {
            if (method == ctorMember) {
                continue;
            }
            String name = method.getName();
            if (name.equals("finishInit")) {
                Class<?>[] parmTypes = method.getParameterTypes();
                if (parmTypes.length == 3
                        && parmTypes[0] == ScriptRuntime.ScriptableClass
                        && parmTypes[1] == FunctionObject.class
                        && parmTypes[2] == ScriptRuntime.ScriptableClass
                        && Modifier.isStatic(method.getModifiers())) {
                    finishInit = method;
                    continue;
                }
            }
            // ignore any compiler generated methods.
            if (name.indexOf('$') != -1) continue;
            if (name.equals(ctorName)) continue;

            Annotation annotation = null;
            String prefix = null;
            if (method.isAnnotationPresent(JSFunction.class)) {
                annotation = method.getAnnotation(JSFunction.class);
            } else if (method.isAnnotationPresent(JSStaticFunction.class)) {
                annotation = method.getAnnotation(JSStaticFunction.class);
            } else if (method.isAnnotationPresent(JSGetter.class)) {
                annotation = method.getAnnotation(JSGetter.class);
            } else if (method.isAnnotationPresent(JSSetter.class)) {
                continue;
            }

            if (annotation == null) {
                if (name.startsWith(functionPrefix)) {
                    prefix = functionPrefix;
                } else if (name.startsWith(staticFunctionPrefix)) {
                    prefix = staticFunctionPrefix;
                } else if (name.startsWith(getterPrefix)) {
                    prefix = getterPrefix;
                } else {
                    // note that setterPrefix is among the unhandled names here -
                    // we deal with that when we see the getter
                    continue;
                }
            }

            boolean isStatic =
                    annotation instanceof JSStaticFunction
                            || Objects.equals(prefix, staticFunctionPrefix);
            HashSet<String> names = isStatic ? staticNames : instanceNames;
            String propName = getPropertyName(name, prefix, annotation);
            if (names.contains(propName)) {
                throw reportRuntimeErrorById("duplicate.defineClass.name", name, propName);
            }
            names.add(propName);
            name = propName;

            if (annotation instanceof JSGetter || Objects.equals(prefix, getterPrefix)) {
                if (!(proto instanceof ScriptableObject)) {
                    throw reportRuntimeErrorById(
                            "msg.extend.scriptable", proto.getClass().toString(), name);
                }
                Method setter = findSetterMethod(methods, name, setterPrefix);
                int attr =
                        ScriptableObject.PERMANENT
                                | ScriptableObject.DONTENUM
                                | (setter != null ? 0 : ScriptableObject.READONLY);
                ((ScriptableObject) proto).defineProperty(name, null, method, setter, attr);
                continue;
            }

            if (isStatic && !Modifier.isStatic(method.getModifiers())) {
                throw Context.reportRuntimeError(
                        "jsStaticFunction must be used with static method.");
            }

            FunctionObject f = new FunctionObject(name, method, proto);
            // TODO
//            if (f.isVarArgsConstructor()) {
//                throw reportRuntimeErrorById("msg.varargs.fun", ctorMember.getName());
//            }
            ScriptableObject.defineProperty(isStatic ? ctor : proto, name, f, ScriptableObject.DONTENUM);
            if (sealed) {
                f.sealObject();
            }
        }

        // Call user code to complete initialization if necessary.
        if (finishInit != null) {
            Object[] finishArgs = {scope, ctor, proto};
            finishInit.invoke(null, finishArgs);
        }

        // Seal the object if necessary.
        if (sealed) {
            ctor.sealObject();
            if (proto instanceof ScriptableObject) {
                ((ScriptableObject) proto).sealObject();
            }
        }

        return ctor;
    }

    protected static Method[] getMethodList(Class<?> clazz) {
        Method[] methods = null;
        try {
            // getDeclaredMethods may be rejected by the security manager
            // but getMethods is more expensive
            if (!sawSecurityException) methods = clazz.getDeclaredMethods();
        } catch (SecurityException e) {
            // If we get an exception once, give up on getDeclaredMethods
            sawSecurityException = true;
        }
        if (methods == null) {
            methods = clazz.getMethods();
        }
        int count = 0;
        for (int i = 0; i < methods.length; i++) {
            if (sawSecurityException
                    ? methods[i].getDeclaringClass() != clazz
                    : !Modifier.isPublic(methods[i].getModifiers())) {
                methods[i] = null;
            } else {
                count++;
            }
        }
        Method[] result = new Method[count];
        int j = 0;
        for (Method method : methods) {
            if (method != null) result[j++] = method;
        }
        return result;
    }

    protected static Method findSingleMethod(Method[] methods, String name) {
        Method found = null;
        for (int i = 0, N = methods.length; i != N; ++i) {
            Method method = methods[i];
            if (method != null && name.equals(method.getName())) {
                if (found != null) {
                    throw reportRuntimeErrorById(
                            "msg.no.overload", name, method.getDeclaringClass().getName());
                }
                found = method;
            }
        }
        return found;
    }

    protected static Member findAnnotatedMember(
            AccessibleObject[] members, Class<? extends Annotation> annotation) {
        for (AccessibleObject member : members) {
            if (member.isAnnotationPresent(annotation)) {
                return (Member) member;
            }
        }
        return null;
    }

    protected static EvaluatorException reportRuntimeErrorById(String messageId, Object... args) {
        String msg = ScriptRuntime.getMessageById(messageId, args);
        return Context.reportRuntimeError(msg);
    }

    protected static Scriptable getTopLevelScope(Scriptable obj) {
        for (; ; ) {
            Scriptable parent = obj.getParentScope();
            if (parent == null) {
                return obj;
            }
            obj = parent;
        }
    }

    @SuppressWarnings({"unchecked"})
    protected static <T extends Scriptable> Class<T> extendsScriptable(Class<?> c) {
        if (ScriptRuntime.ScriptableClass.isAssignableFrom(c)) return (Class<T>) c;
        return null;
    }

    protected static void initAsConstructor(FunctionObject ctor, Scriptable scope, Scriptable prototype, int attributes) {
        ScriptRuntime.setFunctionProtoAndParent(ctor, Context.getCurrentContext(), scope);
        ctor.setImmunePrototypeProperty(prototype);

        prototype.setParentScope(ctor);

        ScriptableObject.defineProperty(prototype, "constructor", ctor, attributes);
        ctor.setParentScope(scope);
    }

    protected static String getPropertyName(String methodName, String prefix, Annotation annotation) {
        if (prefix != null) {
            return methodName.substring(prefix.length());
        }
        String propName = null;
        if (annotation instanceof JSGetter) {
            propName = ((JSGetter) annotation).value();
            if (propName == null || propName.length() == 0) {
                if (methodName.length() > 3 && methodName.startsWith("get")) {
                    propName = methodName.substring(3);
                    if (Character.isUpperCase(propName.charAt(0))) {
                        if (propName.length() == 1) {
                            propName = propName.toLowerCase(Locale.ROOT);
                        } else if (!Character.isUpperCase(propName.charAt(1))) {
                            propName =
                                    Character.toLowerCase(propName.charAt(0))
                                            + propName.substring(1);
                        }
                    }
                }
            }
        } else if (annotation instanceof JSFunction) {
            propName = ((JSFunction) annotation).value();
        } else if (annotation instanceof JSStaticFunction) {
            propName = ((JSStaticFunction) annotation).value();
        }
        if (propName == null || propName.length() == 0) {
            propName = methodName;
        }
        return propName;
    }

    protected static Method findSetterMethod(Method[] methods, String name, String prefix) {
        String newStyleName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method method : methods) {
            JSSetter annotation = method.getAnnotation(JSSetter.class);
            if (annotation != null) {
                if (name.equals(annotation.value())
                        || ("".equals(annotation.value())
                        && newStyleName.equals(method.getName()))) {
                    return method;
                }
            }
        }
        String oldStyleName = prefix + name;
        for (Method method : methods) {
            if (oldStyleName.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

    protected static Scriptable getClassPrototype(BaseFunction function) {
        Object protoVal = function.getPrototypeProperty();
        if (protoVal instanceof Scriptable) {
            return (Scriptable) protoVal;
        }
        return ScriptableObject.getObjectPrototype(function);
    }


}
