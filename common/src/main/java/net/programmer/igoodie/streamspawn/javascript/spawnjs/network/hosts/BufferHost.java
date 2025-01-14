package net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.javascript.base.HostObject;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.core.ConsoleAPI;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSStaticFunction;
import org.mozilla.javascript.typedarrays.NativeArrayBuffer;
import org.mozilla.javascript.typedarrays.NativeArrayBufferView;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class BufferHost extends HostObject {

    protected byte[] buffer;

    public BufferHost() {
        this.buffer = new byte[0];
    }

    @JSConstructor
    public BufferHost(NativeArray buffer) {
        this.buffer = from(buffer).buffer;
    }

    @Override
    public String getClassName() {
        return "Buffer";
    }

    @JSStaticFunction("alloc")
    public static BufferHost _alloc(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);
        Object arg2 = argsAccessor.get(2).orElse(null);

        if (arg0 instanceof Number size) {
            if (arg1 instanceof Number fill) {
                // Buffer.alloc(5, 0xff);
                return bindToScope(alloc(size.intValue(), fill.byteValue()), funObj.getParentScope());
            }
            if (arg1 instanceof String fill) {
                if (arg2 instanceof String encoding) {
                    // Buffer.alloc(11, 'aGVsbG8gd29ybGQ=', 'base64');
                    return bindToScope(alloc(size.intValue(), fill, encoding), funObj.getParentScope());
                }

                // Buffer.alloc(5, 'a');
                return bindToScope(alloc(size.intValue(), fill, "utf8"), funObj.getParentScope());
            }

            // Buffer.alloc(9);
            return bindToScope(alloc(size.intValue(), (byte) 0x0), funObj.getParentScope());
        }

        throw new IllegalArgumentException("Unknown Buffer.alloc parameter compositon: "
                + "(" + ConsoleAPI.stringifyAll(", ", arg0, arg1, arg2) + ")");
    }

    public static BufferHost alloc(int size, byte fill) {
        BufferHost host = new BufferHost();
        host.buffer = new byte[size];
        return host.fill(fill);
    }

    public static BufferHost alloc(int size, String fill, String encoding) {
        BufferHost host = new BufferHost();
        host.buffer = new byte[size];
        return host.fill(fill, encoding);
    }

    @JSStaticFunction("from")
    public static BufferHost _from(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        if (arg0 instanceof NativeArray array) {
            return bindToScope(from(array), funObj.getParentScope());
        }
        if (arg0 instanceof NativeArrayBuffer arrayBuffer) {
            return bindToScope(from(arrayBuffer), funObj.getParentScope());
        }
        if (arg0 instanceof NativeArrayBufferView arrayBufferView) {
            return bindToScope(from(arrayBufferView.getBuffer()), funObj.getParentScope());
        }

        throw new IllegalArgumentException("Unknown Buffer.from parameter compositon: "
                + String.join(", ", Arrays.toString(new Object[]{arg0, arg1})));
    }

    public static BufferHost from(NativeArray array) {
        BufferHost host = new BufferHost();

        byte[] buffer = new byte[array.size()];

        for (int i = 0; i < array.size(); i++) {
            Object element = array.get(i);
            if (element instanceof Number number) {
                buffer[i] = number.byteValue();
            } else if (element instanceof String string) {
                if (string.length() > 1) throw new IllegalArgumentException("Expected ");
//                buffer[i] = ; // TODO
            }
        }

        host.buffer = buffer;
        return host;
    }

    public static BufferHost from(NativeArrayBuffer arrayBuffer) {
        BufferHost host = new BufferHost();
        host.buffer = arrayBuffer.getBuffer();
        return host;
    }

    @JSFunction("fill")
    public static BufferHost _fill(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);
        Object arg2 = argsAccessor.get(2).orElse(null);
        Object arg3 = argsAccessor.get(3).orElse(null);

        BufferHost bufferHost = (BufferHost) thisObj;

        if (arg0 instanceof Number value) {
            if (arg1 instanceof Number offset) {
                if (arg2 instanceof Number end) {
                    // Buffer.alloc(5).fill(0xFF, 0, 5);
                    return bufferHost.fill(value.byteValue(), offset.intValue(), end.intValue());
                }

                // Buffer.alloc(5).fill(0xFF, 0);
                return bufferHost.fill(value.byteValue(), offset.intValue(), bufferHost.buffer.length);
            }

            // Buffer.alloc(5).fill(0xFF);
            return bufferHost.fill(value.byteValue());
        }

        if (arg0 instanceof String value) {
            if (arg1 instanceof Number offset) {
                if (arg2 instanceof Number end) {
                    if (arg3 instanceof String encoding) {
                        return bufferHost.fill(value, offset.intValue(), end.intValue(), encoding);
                    }

                    // Buffer.alloc(5).fill("h", 0, 5);
                    return bufferHost.fill(value, offset.intValue(), end.intValue(), "utf8");
                }

                // Buffer.alloc(5).fill(0xFF, 0);
                return bufferHost.fill(value, offset.intValue(), bufferHost.buffer.length, "utf8");
            }

            // Buffer.alloc(5).fill(0xFF);
            return bufferHost.fill(value, "utf8");
        }

        throw new IllegalArgumentException("Unknown Buffer::fill parameter composition: "
                + "(" + ConsoleAPI.stringifyAll(", ", arg0, arg1, arg2, arg3) + ")");
    }

    public BufferHost fill(byte value) {
        return fill(value, 0, this.buffer.length);
    }

    public BufferHost fill(byte value, int offset, int end) {
        Arrays.fill(this.buffer, offset, end, value);
        return this;
    }

    public BufferHost fill(String value, String encoding) {
        return fill(value, 0, this.buffer.length, encoding);
    }

    public BufferHost fill(String value, int offset, int end, String encoding) {
        if (value.isEmpty()) return fill((byte) 0, offset, end);

        byte[] valueEncoded = Codec.encode(value, encoding);

        for (int i = offset; i < end; i++) {
            this.buffer[i] = valueEncoded[(i - offset) % valueEncoded.length];
        }

        return this;
    }

    public static class Codec {

        public static byte[] encode(String input, String encoding) {
            return switch (encoding.toLowerCase()) {
                case "ascii" -> input.getBytes(StandardCharsets.US_ASCII);
                case "utf8", "utf-8" -> input.getBytes(StandardCharsets.UTF_8);
                case "utf16le", "ucs2" -> input.getBytes(StandardCharsets.UTF_16LE);
                case "latin1", "binary" -> input.getBytes(StandardCharsets.ISO_8859_1);
                case "base64" -> Base64.getDecoder().decode(input);
                case "base64url" -> Base64.getUrlDecoder().decode(input.getBytes(StandardCharsets.UTF_8));
                case "hex" -> encodeHex(input);
                default -> throw new IllegalArgumentException("Unsupported encoding: " + encoding);
            };
        }

        public static String decode(byte[] input, String encoding) {
            return switch (encoding.toLowerCase()) {
                case "ascii" -> new String(input, StandardCharsets.US_ASCII);
                case "utf8", "utf-8" -> new String(input, StandardCharsets.UTF_8);
                case "utf16le", "ucs2" -> new String(input, StandardCharsets.UTF_16LE);
                case "latin1", "binary" -> new String(input, StandardCharsets.ISO_8859_1);
                case "base64" -> new String(Base64.getEncoder().encode(input), StandardCharsets.UTF_8);
                case "base64url" ->
                        new String(Base64.getUrlEncoder().withoutPadding().encode(input), StandardCharsets.UTF_8);
                case "hex" -> decodeHex(input);
                default -> throw new IllegalArgumentException("Unsupported encoding: " + encoding);
            };
        }

        private static byte[] encodeHex(String input) {
            char[] chars = input.toCharArray();
            byte[] hexBytes = new byte[chars.length * 2];
            for (int i = 0; i < chars.length; i++) {
                int value = chars[i];
                hexBytes[i * 2] = (byte) Character.forDigit((value >> 4) & 0xF, 16);
                hexBytes[i * 2 + 1] = (byte) Character.forDigit(value & 0xF, 16);
            }
            return hexBytes;
        }

        private static String decodeHex(byte[] input) {
            StringBuilder sb = new StringBuilder(input.length / 2);
            for (int i = 0; i < input.length; i += 2) {
                int highNibble = Character.digit((char) input[i], 16) << 4;
                int lowNibble = Character.digit((char) input[i + 1], 16);
                sb.append((char) (highNibble | lowNibble));
            }
            return sb.toString();
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<Buffer ");
        for (int i = 0; i < this.buffer.length; i++) {
            byte b = this.buffer[i];
            String hex = Integer.toHexString(Byte.toUnsignedInt(b));
            if (hex.length() < 2) sb.append('0');
            sb.append(hex);
            if (i < this.buffer.length - 1) {
                sb.append(" ");
            }
        }
        sb.append(">");
        return sb.toString();
    }

}
