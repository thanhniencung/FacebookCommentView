package rubikstudio.facebookcommentview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.tumblr.backboard.Actor;
import com.tumblr.backboard.MotionProperty;
import com.tumblr.backboard.imitator.Imitator;
import com.tumblr.backboard.performer.Performer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kiennguyen on 11/10/16.
 */

public class CommentActivity extends AppCompatActivity {

    private List<Comment> commentList = new ArrayList<>();
    private String[] images = new String[] {
        "https://media.licdn.com/mpr/mpr/shrink_100_100/p/7/005/077/104/04f4ff0.jpg",
        "http://choiphongthuy.com/include/timthumb.php?src=uploads/images/news/1455976640_news_3241.jpg&h=100&w=100&zc=1",
        "http://megafun.vn/dataimages/201303/original/images887478_halleberry.jpg",
        "http://megafun.vn/dataimages/201210/original/images795700_amberheard.jpg",
        "http://vip.media.vuathethao.vn/archive/imageslead/199/201610/20161020/thumb7_073729512412687wap_75.jpg",
        "http://megafun.vn/dataimages/201309/original/images986082_100x100.jpg",
        "http://megafun.vn/dataimages/201409/original/images1246461_KateBosworth.jpg",
        "http://www.thegioicaythuoc.com/wp-content/uploads/2016/03/canh-giac-voi-trieu-chung-ho-ra-mau-300x1651-100x100.jpg",
        "http://kenh14cdn.com/2016/1-1459071126609-0-0-100-100-crop-1459071132988.jpg"
    };

    private String[] comments = new String[] {
        "Both she and the DNC were punished for screwing over Bernie and his supporters",
        "@coinsforcharon Low energy. Nah Bro it's all good, The lesser evil has won!",
        "Knew this was coming. Almost posted it myself. Little too late.",
        "Hihihi Clinton loses once more.",
        "I hope we get some descent candidates in 2020. And hopefully she doesn't run as a POTUS again.",
        "Hillary lost the unloseable",
        "Am I the only one disturbed by the way that he is holding up the fishing rod...like...its upside down",
        "@aldo_spy Ummm, no it's not.",
        "@chrisphoenix7 bright side is she won't run again. The white House will never be hers.",
    };

    private String[] names = new String[] {
           "pokekraken",
           "wiseganjalf",
           "jolehollywood",
           "ferretslover",
           "kappakep",
           "slurmsmckenzie",
           "indovim",
           "captnslapaho",
           "labmom"
    };

    private int _yDelta;

    private int getRandomImage() {
        Random rand = new Random();
        return rand.nextInt(images.length);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();

        params.alpha = 1.0f;
        params.dimAmount = 0.5f;

        params.gravity = Gravity.CENTER;
        params.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;

        this.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        setContentView(R.layout.activity_comment);

        for (int i=0; i<9; i++) {
            Comment comment = new Comment();
            comment.content = comments[i];
            comment.avatar = images[i];
            comment.who = names[i];
            comment.time = i + 1 + " minute ago";
            commentList.add(comment);
        }

        ListView listView = (ListView) findViewById(R.id.commentsListView);
        CommentAdapter adapter = new CommentAdapter(this, commentList);
        listView.setAdapter(adapter);

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
    }

    class Comment {
        public String content;
        public String avatar;
        public String who;
        public String time;
    }

    class CommentAdapter extends ArrayAdapter<Comment> {

        public CommentAdapter(Context context, List<Comment> commentList) {
            super(context, 0, commentList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(R.layout.adapter_comment, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.ivAvatar = (ImageView)  convertView.findViewById(R.id.avatar);
                viewHolder.tvContent = (TextView) convertView.findViewById(R.id.content);
                viewHolder.tvWho = (TextView) convertView.findViewById(R.id.who);
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Comment comment = getItem(position);
            viewHolder.tvContent.setText(comment.content);
            viewHolder.tvWho.setText(comment.who);
            viewHolder.tvTime.setText(comment.time);

            Glide.with(CommentActivity.this).load(comment.avatar).into(viewHolder.ivAvatar);
            return convertView;
        }

        class ViewHolder {
            public ImageView ivAvatar;
            public TextView tvContent;
            public TextView tvWho;
            public TextView tvTime;
        }
    }

}
