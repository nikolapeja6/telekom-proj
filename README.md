# Telekom Proj

This repo contains the homework assignment for the [Telekom (Osnovi telekomunikacija, en. Fundamentals of Telecommunications) course][telekom], which is part of the Bachelor's studies at the [School of Electrical Engineering][school], [University of Belgrade][uni].

The homework assignment consisted of writing a desktop app that calculates the compression ratio achieved using 3 methods: Burrows–Wheeler Transform, the Move to Front and the Arithmetic Coder. Optionally, the app can show a step-by-step overview of the compression for a given input.

## Files

The code is divided across two files:

- `Application.java` which represents the UI and contains the entry point for the program. The UI allows the user to select a `*.txt` file as input and specify which phases of the compression (`Burrows-Wheeler`, `Move to Front` or `Arithmetic Coder`) should be displayed in the `output.txt` which is generated when the compression is started. Additional details on how to use the app can be found in the `Help` menu item once the app is started.
- `Compressor.java` which contains the implementation of the different compression methods.

There's also a couple of examples of input files located in the `.\example_inputs`.

## Compiling and Running

In order to compile and run the app, execute the line below:

```console
javac -d .\bin *.java && java -cp .\bin nikolapeja6.Telekom.Application
```

[telekom]: http://telit.etf.rs/kurs/osnovi-telekomunikacija-13e033otr/
[school]: https://www.etf.bg.ac.rs/
[uni]: https://www.bg.ac.rs/
