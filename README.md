# Screenshot

<img src="device-2016-11-10-233237.png" width="480" height="800"/>

# Libraries
```java
compile 'com.facebook.rebound:rebound:0.3.8'
compile 'com.tumblr:backboard:0.1.0'
```

# Implement

Code : 

```java
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);

        new Actor.Builder(SpringSystem.create(), frameLayout)
                .addTranslateMotion(Imitator.TRACK_DELTA, Imitator.FOLLOW_EXACT, MotionProperty.Y)
                .onTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                _yDelta = (int) motionEvent.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                int y = Math.abs((int) motionEvent.getRawY() - _yDelta);
                                if (y > (view.getHeight() * 1) / 4) {
                                    if ((int) motionEvent.getRawY() < _yDelta) {
                                        //HideToTop();
                                    } else {
                                        //HideToBottom();
                                    }
                                }
                                break;

                            case MotionEvent.ACTION_UP:
                                _yDelta = 0;
                                break;
                        }
                        return true;
                    }
                })
                .build();

        SpringSystem springSystem = SpringSystem.create();
        final Spring spring = springSystem.createSpring();
        spring.addListener(new Performer(frameLayout, View.TRANSLATION_Y));

        spring.setEndValue(frameLayout.getMeasuredHeight() - 40);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spring.setEndValue(0);
            }
        }, 400);
```

The important thing is setup a popup theme for comment activity :

```java
    <style name="Theme.PopupTheme" parent="Theme.AppCompat.Light">
        <item name="android:windowFrame">@null</item>
        <item name="android:windowIsFloating">false</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowSoftInputMode">stateAlwaysHidden</item>
        <item name="android:colorBackgroundCacheHint">@null</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="actionBarStyle">@null</item>
    </style>
```
