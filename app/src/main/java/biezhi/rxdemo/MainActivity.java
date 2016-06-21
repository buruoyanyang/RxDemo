package biezhi.rxdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observer<String> observe = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d("tag", "Completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("tag", "Error");

            }

            @Override
            public void onNext(String s) {
                Log.d("tag", "Item" + s);
            }
        };
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onStart() {
                //用于开始之前的处理
            }
        };
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("World");
                subscriber.onNext("!");
                subscriber.onCompleted();
            }
        });
        //快速创建
        Observable observable1 = Observable.just("Hello", "World", "!");
        String[] words = {"Hello", "World", "!"};
        Observable observable2 = Observable.from(words);
        //做订阅绑定工作
//        observable.subscribe(observe);
//        observable.subscribe(subscriber);

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("tag", s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        };
        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.d("tag", "completed");
            }
        };
        observable.subscribe(onNextAction);
        observable.subscribe(onNextAction, onErrorAction);
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
        final int drawableRes = R.drawable.lock;
        final ImageView imageView = (ImageView) findViewById(R.id.image);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Drawable drawable = getTheme().getDrawable(drawableRes);
                    subscriber.onNext(drawable);
                    subscriber.onCompleted();
                }

            }
        })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        assert imageView != null;
                        imageView.setImageDrawable(drawable);

                    }
                });

        final ImageView imageView1 = (ImageView)findViewById(R.id.image1);
        //被创建的事件内容1，2，3，4将会在IO线程发出，
        //subscribe数字的答应将发生在主线程
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("tag", "number" + null);
                    }
                });
        Observable.just(R.drawable.chanel_press)
                .map(new Func1<Integer, Bitmap>() {
                    @Override
                    public Bitmap call(Integer integer) {
                        return BitmapFactory.decodeResource(getResources(),integer);
                    }
                })
        .subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                assert imageView1 != null;
                imageView1.setImageBitmap(bitmap);
            }
        })
        ;

    }


}





























