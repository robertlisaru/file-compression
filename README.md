## file-compression

<img src="https://github.com/robertlisaru/file-compression/assets/40792547/c220b6ce-8a18-4fb7-8be7-3fe77c3f18e4" width=200px/>


Java implementation of the Huffman, LZ77, and LZW compression algorithms. 

![image](https://github.com/robertlisaru/file-compression/assets/40792547/0008495d-1fa0-421e-9017-dfba413b664d)


### Benchmarks

```
method: huffman
file to compress: src\main\java\ro\ulbsibiu\ccsd\laboratory\robert\ui\MainFrame.java
file size: 33.3kb
compressed file size: 16.8kb
ratio: 50%
```

```
method: lz77
offset size: 15bits
length size: 7bits
file to compress: src\main\java\ro\ulbsibiu\ccsd\laboratory\robert\ui\MainFrame.java
file size: 33.3kb
compressed file size: 6.3kb
ratio: 19%
```

```
method: lzw
dictionary size: 15bits
dictionary strategy: freeze
file to compress: src\main\java\ro\ulbsibiu\ccsd\laboratory\robert\ui\MainFrame.java
file size: 33.3kb
compressed file size: 11.3kb
ratio: 34%
```
