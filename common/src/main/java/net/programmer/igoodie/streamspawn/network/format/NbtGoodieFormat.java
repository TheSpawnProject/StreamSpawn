package net.programmer.igoodie.streamspawn.network.format;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.*;
import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.*;
import net.programmer.igoodie.goodies.util.StringUtilities;

import java.util.stream.Collectors;

public class NbtGoodieFormat extends GoodieFormat<CompoundTag, GoodieObject> {

    public static final NbtGoodieFormat INSTANCE = new NbtGoodieFormat();

    public static final String NULL_SUFFIX = "_____NULL";
    public static final String BOOL_SUFFIX = "_____BOOL";
    public static final String CHAR_SUFFIX = "_____CHAR";

    private NbtGoodieFormat() {}

    /* ------------------------------- */

    @Override
    public GoodieObject writeToGoodie(CompoundTag compoundTag) {
        return convertCompoundTag(compoundTag);
    }

    public static GoodieElement convert(Tag tag) {
        if (tag instanceof CompoundTag)
            return convertCompoundTag(((CompoundTag) tag));
        if (tag instanceof CollectionTag<?>)
            return convertCollection(((CollectionTag<?>) tag));
        if (tag instanceof NumericTag)
            return convertNumeric(((NumericTag) tag));
        if (tag instanceof StringTag)
            return convertString(((StringTag) tag));

        return null; // <-- No corresponding Goodie type exists
    }

    public static GoodieObject convertCompoundTag(CompoundTag compoundTag) {
        GoodieObject goodieObject = new GoodieObject();
        for (String key : compoundTag.getAllKeys()) {
            Tag element = compoundTag.get(key);
            assert element != null;

            String propertyName = key;
            GoodieElement goodieElement = convert(element);

            if (key.endsWith(NULL_SUFFIX)) {
                goodieElement = GoodieNull.INSTANCE;
                propertyName = StringUtilities.shrink(propertyName, 0, NULL_SUFFIX.length());
            } else if (key.endsWith(BOOL_SUFFIX) && goodieElement.isPrimitive()) {
                GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();
                if (goodiePrimitive.isNumber()) {
                    goodieElement = new GoodiePrimitive(goodiePrimitive.getByte() == 1);
                    propertyName = StringUtilities.shrink(propertyName, 0, BOOL_SUFFIX.length());
                }
            } else if (key.endsWith(CHAR_SUFFIX) && goodieElement.isPrimitive()) {
                GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();
                if (goodiePrimitive.isString() && goodiePrimitive.getString().length() != 0) {
                    goodieElement = new GoodiePrimitive(goodiePrimitive.getString().charAt(0));
                    propertyName = StringUtilities.shrink(propertyName, 0, CHAR_SUFFIX.length());
                }
            }

            goodieObject.put(propertyName, goodieElement);
        }
        return goodieObject;
    }

    public static GoodieArray convertCollection(CollectionTag<?> collectionTag) {
        GoodieArray goodieArray = new GoodieArray();
        collectionTag.forEach(tag -> {
            GoodieElement goodieElement = convert(tag);
            goodieArray.add(goodieElement);
        });
        return goodieArray;
    }

    public static GoodiePrimitive convertNumeric(NumericTag tag) {
        return new GoodiePrimitive(tag instanceof ByteTag ? tag.getAsByte()
                : tag instanceof ShortTag ? tag.getAsShort()
                : tag instanceof IntTag ? tag.getAsInt()
                : tag instanceof LongTag ? tag.getAsLong()
                : tag instanceof FloatTag ? tag.getAsFloat()
                : tag instanceof DoubleTag ? tag.getAsDouble()
                : tag.getAsNumber());
    }

    public static GoodieElement convertString(StringTag tag) {
        return GoodiePrimitive.from(tag.getAsString());
    }

    /* ------------------------------- */

    @Override
    public CompoundTag readFromGoodie(GoodieObject goodieObject) {
        return convertObject(goodieObject);
    }

    public static Tag convert(GoodieElement goodieElement) {
        if (goodieElement.isObject())
            return convertObject(goodieElement.asObject());
        if (goodieElement.isArray())
            return convertArray(goodieElement.asArray());
        if (goodieElement.isPrimitive() && goodieElement.asPrimitive().isNumber())
            return convertNumber(goodieElement.asPrimitive());
        if (goodieElement.isPrimitive() && goodieElement.asPrimitive().isString())
            return convertString(goodieElement.asPrimitive());
        if (goodieElement.isPrimitive() && goodieElement.asPrimitive().isCharacter())
            return convertCharacter(goodieElement.asPrimitive());
        if (goodieElement.isPrimitive() && goodieElement.asPrimitive().isBoolean())
            return convertBoolean(goodieElement.asPrimitive());
        if (goodieElement.isNull())
            return ByteTag.ZERO;


        return null; // <-- No corresponding NBT type exists
    }

    public static CompoundTag convertObject(GoodieObject goodieObject) {
        CompoundTag compoundTag = new CompoundTag();
        for (String propertyName : goodieObject.keySet()) {
            GoodieElement goodieElement = goodieObject.get(propertyName);
            String tagKey = propertyName;

            if (goodieElement.isNull()) {
                tagKey += NULL_SUFFIX;
            } else if (goodieElement.isPrimitive() && goodieElement.asPrimitive().isBoolean()) {
                tagKey += BOOL_SUFFIX;
            } else if (goodieElement.isPrimitive() && goodieElement.asPrimitive().isCharacter()) {
                tagKey += CHAR_SUFFIX;
            }

            Tag tag = convert(goodieElement);
            if (tag != null) compoundTag.put(tagKey, tag);
        }
        return compoundTag;
    }

    public static CollectionTag<?> convertArray(GoodieArray goodieArray) {
        if (goodieArray.stream().allMatch(elem -> elem.isPrimitive()
                && elem.asPrimitive().isNumber()
                && elem.asPrimitive().getNumber() instanceof Byte)) {
            return new ByteArrayTag(goodieArray.stream()
                    .map(goodieElement -> goodieElement.asPrimitive().getNumber().byteValue())
                    .collect(Collectors.toList()));
        }

        if (goodieArray.stream().allMatch(elem -> elem.isPrimitive()
                && elem.asPrimitive().isNumber()
                && elem.asPrimitive().getNumber() instanceof Integer)) {
            return new IntArrayTag(goodieArray.stream()
                    .map(goodieElement -> goodieElement.asPrimitive().getNumber().intValue())
                    .collect(Collectors.toList()));
        }

        if (goodieArray.stream().allMatch(elem -> elem.isPrimitive()
                && elem.asPrimitive().isNumber()
                && elem.asPrimitive().getNumber() instanceof Long)) {
            return new LongArrayTag(goodieArray.stream()
                    .map(goodieElement -> goodieElement.asPrimitive().getNumber().longValue())
                    .collect(Collectors.toList()));
        }

        ListTag listTag = new ListTag();
        for (GoodieElement goodieElement : goodieArray) {
            Tag tag = convert(goodieElement);
            listTag.add(tag);
        }
        return listTag;
    }

    public static NumericTag convertNumber(GoodiePrimitive goodiePrimitive) {
        Number number = goodiePrimitive.getNumber();

        if (number instanceof Byte)
            return ByteTag.valueOf(number.byteValue());
        if (number instanceof Short)
            return ShortTag.valueOf(number.shortValue());
        if (number instanceof Integer)
            return IntTag.valueOf(number.intValue());
        if (number instanceof Long)
            return LongTag.valueOf(number.longValue());
        if (number instanceof Float)
            return FloatTag.valueOf(number.floatValue());
        if (number instanceof Double)
            return DoubleTag.valueOf(number.doubleValue());

        return null; // <- Like.. I don't even know what is wrong then
    }

    public static StringTag convertString(GoodiePrimitive goodiePrimitive) {
        return StringTag.valueOf(goodiePrimitive.getString());
    }

    public static StringTag convertCharacter(GoodiePrimitive goodiePrimitive) {
        return StringTag.valueOf(goodiePrimitive.getCharacter() + "");
    }

    public static ByteTag convertBoolean(GoodiePrimitive goodiePrimitive) {
        return ByteTag.valueOf(goodiePrimitive.getBoolean());
    }

    /* ------------------------------- */

    @Override
    public String writeToString(CompoundTag compoundTag, boolean pretty) {
        return compoundTag.getAsString();
    }

    @Override
    public CompoundTag readFromString(String string) throws GoodieParseException {
        try {
            return new TagParser(new StringReader(string)).readStruct();
        } catch (CommandSyntaxException e) {
            throw new GoodieParseException("Error while trying to parse SNBT", e);
        }
    }

    /* --------------------- */

//    public static void main(String[] args) throws Exception {
//        GoodieObject goodie = new GoodieObject();
//        goodie.put("bool1", false);
//        goodie.put("bool2", true);
//        goodie.put("nullie", GoodieNull.INSTANCE);
//        goodie.put("char", 'a');
//
//        System.out.println(INSTANCE.writeToString(goodie, true));
//        System.out.println(INSTANCE.writeToGoodie(INSTANCE.readFromGoodie(goodie)));
//
//        CompoundTag compoundTag = TagParser.parseTag("{ " +
//                "id: 12345L, " +
//                "byte: 127b, " +
//                "byteArray: [B; 1b, 2b, 3b, 5b], " +
//                "intArray: [I; 6, 7, 8], " +
//                "longArray: [L; 6l, 7l, 8l], " +
//                "string: 'Hey there!', " +
//                "stringArray: ['A', 'B', 'C'], " +
//                "compound: { a: 1, b: 2, c: [3, 4] }" +
//                "}");
//        GoodieObject goodieObject = INSTANCE.writeToGoodie(compoundTag);
//
//        System.out.println("NBT   : " + compoundTag.getAsString());
//        System.out.println("Goodie: " + goodieObject);
//        System.out.println("NBT   : " + INSTANCE.readFromGoodie(goodieObject));
//        System.out.println("----");
//        System.out.println("From NBT   : " + INSTANCE.writeToString(compoundTag, true));
//        System.out.println("From Goodie: " + INSTANCE.writeToString(goodieObject, true));
//    }

}