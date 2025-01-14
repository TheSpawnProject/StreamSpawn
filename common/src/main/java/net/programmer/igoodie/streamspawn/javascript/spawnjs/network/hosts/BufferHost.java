package net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts;

import net.programmer.igoodie.streamspawn.javascript.base.HostObject;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.core.ConsoleAPI;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Undefined;
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
    protected BufferHost(int[] buffer) {
        byte[] byteBuffer = new byte[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            byteBuffer[i] = (byte) (buffer[i]);
        }
        this.buffer = byteBuffer;
    }

    @Override
    public String getClassName() {
        return "Buffer";
    }

    @JSStaticFunction("alloc")
    public static BufferHost _alloc(Object arg0, Object arg1, Object arg2) {
        if (arg0 instanceof Number size) {
            if (arg1 instanceof Number fill) {
                // Buffer.alloc(5, 0xff);
                return alloc(size.intValue(), fill.byteValue());
            }
            if (arg1 instanceof String fill) {
                if (arg2 instanceof String encoding) {
                    // Buffer.alloc(11, 'aGVsbG8gd29ybGQ=', 'base64');
                    return alloc(size.intValue(), fill, encoding);
                }

                // Buffer.alloc(5, 'a');
                return alloc(size.intValue(), fill);
            }

            // Buffer.alloc(9);
            return alloc(size.intValue());
        }

        throw new IllegalArgumentException("Unknown Buffer.alloc parameter compositon: "
                + "(" + ConsoleAPI.stringifyAll(", ", arg0, arg1, arg2) + ")");
    }

    public static BufferHost alloc(int size) {
        return alloc(size, (byte) 0x0);
    }

    public static BufferHost alloc(int size, byte fill) {
        BufferHost host = new BufferHost();
        host.buffer = new byte[size];
        return host.fill(fill);
    }

    public static BufferHost alloc(int size, String fill) {
        return alloc(size, fill, "utf8");
    }

    public static BufferHost alloc(int size, String fill, String encoding) {
        BufferHost host = new BufferHost();
        host.buffer = new byte[size];
        return host.fill(fill, encoding);
    }

    @JSStaticFunction
    public static BufferHost from(Object arg0, Object arg1) {
        if (arg0 instanceof NativeArray array) {
            return fromArray(array);
        }
        if (arg0 instanceof NativeArrayBuffer arrayBuffer) {
            return fromArrayBuffer(arrayBuffer);
        }
        if (arg0 instanceof NativeArrayBufferView arrayBufferView) {
            return fromArrayBuffer(arrayBufferView.getBuffer());
        }

        throw new IllegalArgumentException("Unknown Buffer.from parameter compositon: "
                + String.join(", ", Arrays.toString(new Object[]{arg0, arg1})));
    }

    protected static BufferHost fromArray(NativeArray array) {
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

    protected static BufferHost fromArrayBuffer(NativeArrayBuffer arrayBuffer) {
        BufferHost host = new BufferHost();
        host.buffer = arrayBuffer.getBuffer();
        return host;
    }

    @JSFunction("fill")
    public BufferHost _fill(Object arg0, Object arg1, Object arg2, Object arg3) {
        if (arg1 == Undefined.instance) arg1 = 0;
        if (arg2 == Undefined.instance) arg2 = buffer.length;
        if (arg3 == Undefined.instance) arg3 = "ascii";

        if (arg0 instanceof Number number) {
//            for (int i = arg1; i < arg2; i++) {
//                this.buffer[i] = number.byteValue();
//            }
//            return this;
        }

        if (arg0 instanceof String string) {

        }

        throw new IllegalArgumentException("Unknown Buffer::fill parameter composition: "
                + String.join(", ", Arrays.toString(new Object[]{arg0, arg1, arg2, arg3})));
    }

    public BufferHost fill(byte value) {
        return fill(value, 0, this.buffer.length);
    }

    public BufferHost fill(byte value, int offset, int end) {
        Arrays.fill(this.buffer, offset, end, value);
        return this;
    }

    public BufferHost fill(String value) {
        return fill(value, "utf8");
    }

    public BufferHost fill(String value, String encoding) {
        return fill(value, 0, this.buffer.length, encoding);
    }

    public BufferHost fill(String value, int offset, int end, String encoding) {
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
