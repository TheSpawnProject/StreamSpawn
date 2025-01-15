console.log(new Buffer([0x1, 0x2, 0x3]));
console.log(Buffer.alloc(11, "aGVsbG8gd29ybGQ=", "base64"));
console.log(Buffer.alloc(9));
console.log(Buffer.alloc(5, 1700));
console.log(Buffer.alloc(5, "a"));
console.log(Buffer.alloc(5, 0xff));

console.log("---");

console.log(Buffer.alloc(6).fill(0xff));

console.log("---");

console.log(Buffer.from([0x1, 0x2, 0x3]));

const arr = new Uint16Array(2);
arr[0] = 5000;
arr[1] = 4000;

const buf = Buffer.from(arr.buffer);
console.log(buf);
arr[1] = 6000;
console.log(buf);

const ab = new ArrayBuffer(10);
console.log(Buffer.from(ab, 0, 2));
