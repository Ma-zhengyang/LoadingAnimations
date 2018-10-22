# LoadingAnimations

## Demo

![Screenshot](screenshots/preview.gif)

## Usage

### Step 1

Add dependencies in build.gradle.

```gradle
    compile 'com.example.mzy.indicators:LoadingAnimations:1.0.0'
```


### Step 2

Add the LoadingAnimations to your layout:

e.g. 

```xml
    <com.example.mzy.indicators.LoadingIndicator
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:indicatorName="ParallelogramIndicator" />
```

## Indicators

As seen above in the **Demo**, the indicators are as follows:

**Row 1**
 * `BasketBallIndicator`
 * `StarIndicator`
 * `BallScaleIndicator`
 * `DropIndicator`

**Row 2**
 * `CollisionIndicator`
 * `ChartRectIndicator1`
 * `ChartRectIndicator2`
 * `ParallelogramIndicator`

**Row 3**
 * `DoubleArcIndicator`
 * `DoubleArcScaleRotateIndicator`


