# Installation of JMeter Libraries

This directory contains necessary third-party libraries for the JMeter test plans in this project.

## Installation Steps

To use these libraries, you need to copy them into the `lib/ext` directory of your Apache JMeter installation. This allows JMeter to load these `.jar` files when it starts.

### macOS Instructions

The location of the `lib/ext` directory can vary depending on how you installed JMeter.

**1. Standard Installation (from .dmg file):**

If you installed JMeter by downloading it from the official website and dragging it to your Applications folder, the path is usually:

```
/Applications/Apache JMeter.app/Contents/Resources/app/lib/ext
```

**2. Homebrew Installation:**

If you installed JMeter using Homebrew, the path will look something like this (the version number might be different):

```
/usr/local/Cellar/jmeter/5.6.3/libexec/lib/ext
```

You can find the exact path by running `brew --prefix jmeter` and then navigating to the `libexec/lib/ext` subdirectory.

**Action:**

Copy all `.jar` files from this `Lib` folder into one of the paths mentioned above.

### Windows Instructions

1.  Find the directory where you installed or unzipped Apache JMeter.
2.  Inside that directory, navigate to the `lib\ext` folder. For example:

    ```
    C:\apache-jmeter-5.6.3\lib\ext
    ```

**Action:**

Copy all `.jar` files from this `Lib` folder into the `lib\ext` directory.

---

After copying the files, **restart JMeter** for the changes to take effect.
