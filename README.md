# PinView

![JitPack](https://img.shields.io/jitpack/v/github/niTots/PinView)

<p align="center">
<img width="335" alt="Screenshot 2022-06-19 at 16 07 33" src="https://user-images.githubusercontent.com/17580508/174485486-058b2a93-937b-42a2-a3d8-fb3724669dae.png">
</p>

## What is this?
This is the repository of the awesome and highly customizable PinView for the Android Platform.

## Download
PinView is available on `jitpack.io`.
To use it as a dependency, don't forget to:
1. Declare `jitpack.io` as a repository in your `build.gradle (project level)`;

```
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```
2. Decalre the dependency:

```
implementation 'com.github.niTots:PinView:1.0.0' 
```

## How to use?
### Configuring
First of all, declare this 3 attributes inside your theme with default styles:
```
    <item name="pinSetViewStyle">@style/Widget.NiTots.PinSetView</item>
    <item name="pinViewStyle">@style/Widget.NiTots.PinView</item>
    <item name="pinSetErrorLabelStyle">@style/Widget.NiTots.PinSetView.ErrorLabel</item>
```

The only class, required to be used, is `PinSetView`. You can either use it inside your `.xml` files or directly from code.
</p>
Don't forget to set the completion listener. It will be called, when the last pin is entered.

```
pinSetView.setCompletionListener{
  ...
}
```
In case the pin is incorrect, call `incorrectPinEntered()` function.

### Styling
The `PinView` provides many styling options. You can:
- Apply custom amount of pins: The attribute `pinSet_pinCount`;
- Choose the size of the `PinView` from: `large`, `medium` or `small`. The attribute `pinSet_size`;
- Apply custom horizontal margin between pins. The attribute `pinSet_horizontalMargin`;
- Choose whether to show the error lable or not. The attribute `pinSet_isLabelShown`;
- Choose the position of the error lable from: `above` or `below`. The attribute `pinSet_labelPosition`;
- Apply custom vertival margin for the error label. The attribute `pinSet_labelMargin`;
- Choose the size of the shape from: `large`, `medium` or `small`. The attribute `pin_size`;
- Apply custom color for the shape. The attribute `pin_color`;
- Choose the shape to be drawn from: `square`, `rounded` or `circle`. The attribute `pin_drawingStyle`;
- Choose the animation from: `common`, `bouncing` or `coloring`. The attribute `pin_animationStyle`;
- Apply custom animation duration. The attribute `pin_animationDuration`.

All the parameters have the default values, which are declared in the proper style. </p>
In case you want to customize it (:fire: of course you want :fire:), you need to do the following:
- Create custom style and extend it from one of the build-in from this library;
- Inside the theme, place your style within the proper theme attribute. (Theme attributes are listed in [Configuring](#configuring) section.)


### Available styles
There are 3 built-in styles:
- For managing the **PinSetView** properties: `"Widget.NiTots.PinSetView"`;
- For managing the individual pin view properties: `"Widget.NiTots.PinView"`;
- For managing the error label properties: `"Widget.NiTots.PinSetView.ErrorLabel"`.
</p>

Need to notice:
- The style `"Widget.NiTots.PinView"` extend from `"Widget.MaterialComponents.CardView"`, so any properties, that can be used for the `MaterialCardView` are available insde this style too.
- The style `"Widget.NiTots.PinSetView.ErrorLabel"` extend from `"Widget.MaterialComponents.TextView"`, so any properties, that can be used for the `TextView` are available insde this style too.

## License
```
MIT License

Copyright (c) [2022] [niTots]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
