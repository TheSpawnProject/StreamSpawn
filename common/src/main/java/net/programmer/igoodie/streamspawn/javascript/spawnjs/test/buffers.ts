console.log(new Buffer([0x1, 0x2, 0x3]));
console.log(Buffer.alloc(11, "aGVsbG8gd29ybGQ=", "base64"));
console.log(Buffer.alloc(9));
console.log(Buffer.alloc(5, 1700));
console.log(Buffer.alloc(5, "a"));
console.log(Buffer.alloc(5, 0xff));

console.log("---");

console.log(Buffer.alloc(6).fill(0xff));
