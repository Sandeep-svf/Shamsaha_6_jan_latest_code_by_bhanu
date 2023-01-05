package com.shamsaha.app.adaptor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.Message.ChatMessage;
import com.shamsaha.app.activity.ChatHelper.Message.DateFormatter;
import com.shamsaha.app.activity.ChatHelper.Message.UserMessage;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Message;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;


public class ChatAdaptor extends RecyclerView.Adapter {

    private Context context;
    private List<Message> messages;
    private String author;
    private UserMessage userMessage;
    private List<ChatMessage> messagess;
    private Dialog dialog;
    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";


    public ChatAdaptor(Context context, List<Message> messages, String author) {
        this.context = context;
        this.messages = messages;
        this.author = author;
    }

    @Override
    public long getItemId(int position) {
        if (messages.get(position).getAuthor().contains(author)) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getAuthor().contains(author)) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.right_chat, parent, false);
            ViewHolderL holderL = new ViewHolderL(view);
            return holderL;
        }

        view = layoutInflater.inflate(R.layout.left_chat, parent, false);
        return new ViewHolderR(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        Log.d("mmmmm", "========================================================================");
//        Log.d("mmmmm", messages.get(position).getType().toString());
//        Log.d("mmmmm", "========================================================================");
        if (messages.get(position).getAuthor().contains(author)) {
            ViewHolderL viewHolderL = (ViewHolderL) holder;
            viewHolderL.imageView2.setVisibility(View.GONE);
            if (messages.get(position).getType().toString().equals("MEDIA")) {
                messages.get(position).getMedia().getContentTemporaryUrl(new CallbackListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("mmmmm", "onSuccess: " + s);
                        viewHolderL.imageView2.setVisibility(View.VISIBLE);
                        viewHolderL.richLinkView.setVisibility(View.GONE);
                        viewHolderL.progressBar.setVisibility(View.GONE);
                        if (messages.get(position).getMedia().getFileName().endsWith(".jpg") || messages.get(position).getMedia().getFileName().endsWith(".png")
                                || messages.get(position).getMedia().getFileName().endsWith(".jpeg")) {
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderL.imageView2);
                        } else if (messages.get(position).getMedia().getFileName().endsWith(".mp4") || messages.get(position).getMedia().getFileName().endsWith(".3gp")) {
                            viewHolderL.imageView2.setVisibility(View.VISIBLE);
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(R.drawable.ic_play).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderL.imageView2);
                        } else if (messages.get(position).getMedia().getFileName().endsWith(".mp3")) {
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(R.drawable.ic_play).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderL.imageView2);
                        } else if (messages.get(position).getMedia().getFileName().endsWith(".txt") || messages.get(position).getMedia().getFileName().endsWith(".docx")
                                || messages.get(position).getMedia().getFileName().endsWith(".pdf") || messages.get(position).getMedia().getFileName().endsWith(".apk")
                                || messages.get(position).getMedia().getFileName().endsWith(".xml") || messages.get(position).getMedia().getFileName().endsWith(".xlsx")
                                || messages.get(position).getMedia().getFileName().endsWith(".ppt")) {
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(R.drawable.ic_google_docs).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderL.imageView2);
                        }
                        if (messages.get(position).getMessageBody().equals("")) {
                            viewHolderL.textView.setVisibility(View.GONE);
                        }
                        viewHolderL.imageView2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (messages.get(position).getMedia().getFileName().endsWith(".jpg") || messages.get(position).getMedia().getFileName().endsWith(".png")
                                        || messages.get(position).getMedia().getFileName().endsWith(".jpeg")) {
                                    settingsDialog(s, 0);
                                } else if (messages.get(position).getMedia().getFileName().endsWith(".mp4") || messages.get(position).getMedia().getFileName().endsWith(".3gp")) {
                                    settingsDialog(s, 1);
                                } else if (messages.get(position).getMedia().getFileName().endsWith(".mp3")) {
                                    settingsDialog(s, 1);
                                } else if (messages.get(position).getMedia().getFileName().endsWith(".txt") || messages.get(position).getMedia().getFileName().endsWith(".docx")
                                        || messages.get(position).getMedia().getFileName().endsWith(".pdf") || messages.get(position).getMedia().getFileName().endsWith(".apk")
                                        || messages.get(position).getMedia().getFileName().endsWith(".xml") || messages.get(position).getMedia().getFileName().endsWith(".xlsx")
                                        || messages.get(position).getMedia().getFileName().endsWith(".ppt")) {
                                    browserIntent(s);
                                }
                            }
                        });
                    }
                });
            }
            if (messages.get(position).getType().toString().equals("TEXT") && messages.get(position).getMessageBody().startsWith("Current Location")) {
                viewHolderL.textView.setVisibility(View.GONE);
                viewHolderL.richLinkView.setVisibility(View.VISIBLE);
                viewHolderL.progressBar.setVisibility(View.VISIBLE);
                viewHolderL.imageView2.setVisibility(View.GONE);
                String URL = messages.get(position).getMessageBody().replace("Current Location\n\n", "");
                viewHolderL.richLinkView.setLink(URL, new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {
                        viewHolderL.imageView2.setVisibility(View.GONE);
                        viewHolderL.progressBar.setVisibility(View.GONE);
                        Log.e("vhdvj", String.valueOf(status));
                    }

                    @Override
                    public void onError(Exception e) {
                        viewHolderL.imageView2.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                });
            }
            if (messages.get(position).getType().toString().equals("TEXT") && messages.get(position).getMessageBody().startsWith("https://")) {
                viewHolderL.textView.setVisibility(View.GONE);
                viewHolderL.richLinkView.setVisibility(View.VISIBLE);
                viewHolderL.progressBar.setVisibility(View.VISIBLE);
                viewHolderL.imageView2.setVisibility(View.GONE);
                viewHolderL.richLinkView.setLink(messages.get(position).getMessageBody(), new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {
                        viewHolderL.imageView2.setVisibility(View.GONE);
                        viewHolderL.progressBar.setVisibility(View.GONE);
                        Log.e("vhdvj", String.valueOf(status));
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            viewHolderL.textView.setText(messages.get(position).getMessageBody());
            viewHolderL.textView4.setText(messages.get(position).getAuthor());
            viewHolderL.textView5.setText(DateFormatter.getFormattedDateFromISOString(messages.get(position).getDateCreated()));
        } else {
            ViewHolderR viewHolderR = (ViewHolderR) holder;
            viewHolderR.imageView.setVisibility(View.GONE);
            if (messages.get(position).getType().toString().equals("MEDIA")) {
                messages.get(position).getMedia().getContentTemporaryUrl(new CallbackListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("mmmmm", "onSuccess: " + s);
                        viewHolderR.imageView.setVisibility(View.VISIBLE);
                        viewHolderR.richLinkView.setVisibility(View.GONE);
                        viewHolderR.progressBar.setVisibility(View.GONE);
                        if (messages.get(position).getMedia().getFileName().endsWith(".jpg") || messages.get(position).getMedia().getFileName().endsWith(".png")
                                || messages.get(position).getMedia().getFileName().endsWith(".jpeg")) {
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderR.imageView);
                        } else if (messages.get(position).getMedia().getFileName().endsWith(".mp4") || messages.get(position).getMedia().getFileName().endsWith(".3gp")) {
                            viewHolderR.imageView.setVisibility(View.VISIBLE);
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(R.drawable.ic_play).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderR.imageView);
                        } else if (messages.get(position).getMedia().getFileName().endsWith(".mp3")) {
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(R.drawable.ic_play).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderR.imageView);
                        } else if (messages.get(position).getMedia().getFileName().endsWith(".txt") || messages.get(position).getMedia().getFileName().endsWith(".docx")
                                || messages.get(position).getMedia().getFileName().endsWith(".pdf") || messages.get(position).getMedia().getFileName().endsWith(".apk")
                                || messages.get(position).getMedia().getFileName().endsWith(".xml") || messages.get(position).getMedia().getFileName().endsWith(".xlsx")
                                || messages.get(position).getMedia().getFileName().endsWith(".ppt")) {
                            if (TwilioChatApplication.get() != null)
                                Glide.with(TwilioChatApplication.get()).load(R.drawable.ic_google_docs).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderR.imageView);
                        }

                        if (messages.get(position).getMessageBody().equals("")) {
                            viewHolderR.textView.setVisibility(View.GONE);
                        }
                        viewHolderR.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (messages.get(position).getMedia().getFileName().endsWith(".jpg") || messages.get(position).getMedia().getFileName().endsWith(".png")
                                        || messages.get(position).getMedia().getFileName().endsWith(".jpeg")) {
                                    settingsDialog(s, 0);
                                } else if (messages.get(position).getMedia().getFileName().endsWith(".mp4") || messages.get(position).getMedia().getFileName().endsWith(".3gp")) {
                                    settingsDialog(s, 1);
                                } else if (messages.get(position).getMedia().getFileName().endsWith(".mp3")) {
                                    settingsDialog(s, 1);
                                } else if (messages.get(position).getMedia().getFileName().endsWith(".txt") || messages.get(position).getMedia().getFileName().endsWith(".docx")
                                        || messages.get(position).getMedia().getFileName().endsWith(".pdf") || messages.get(position).getMedia().getFileName().endsWith(".apk")
                                        || messages.get(position).getMedia().getFileName().endsWith(".xml") || messages.get(position).getMedia().getFileName().endsWith(".xlsx")
                                        || messages.get(position).getMedia().getFileName().endsWith(".ppt")) {
                                    browserIntent(s);
                                }
                            }
                        });
                    }
                });
            }
            if (messages.get(position).getType().toString().equals("TEXT") && messages.get(position).getMessageBody().startsWith("Current Location")) {
                viewHolderR.textView.setVisibility(View.GONE);
                viewHolderR.richLinkView.setVisibility(View.VISIBLE);
                viewHolderR.imageView.setVisibility(View.GONE);
                viewHolderR.progressBar.setVisibility(View.VISIBLE);
                String URL = messages.get(position).getMessageBody().replace("Current Location\n\n", "");
                viewHolderR.richLinkView.setLink(URL, new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {
                        viewHolderR.imageView.setVisibility(View.GONE);
                        viewHolderR.progressBar.setVisibility(View.GONE);
                        Log.e("vhdvj", String.valueOf(status));
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            if (messages.get(position).getType().toString().equals("TEXT") && messages.get(position).getMessageBody().startsWith("https://")) {
                viewHolderR.textView.setVisibility(View.GONE);
                viewHolderR.richLinkView.setVisibility(View.VISIBLE);
                viewHolderR.progressBar.setVisibility(View.VISIBLE);
                viewHolderR.imageView.setVisibility(View.GONE);
                viewHolderR.richLinkView.setLink(messages.get(position).getMessageBody(), new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {
                        viewHolderR.imageView.setVisibility(View.GONE);
                        viewHolderR.progressBar.setVisibility(View.GONE);
                        Log.e("vhdvj", String.valueOf(status));
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            viewHolderR.textView.setText(messages.get(position).getMessageBody());
            viewHolderR.textView2.setText(messages.get(position).getAuthor());
            viewHolderR.textView3.setText(DateFormatter.getFormattedDateFromISOString(messages.get(position).getDateCreated()));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        userMessage = new UserMessage(message);
        messagess.add(userMessage);
        notifyDataSetChanged();
    }

    static class ViewHolderL extends RecyclerView.ViewHolder {

        TextView textView, textView4, textView5;
        ImageView imageView2;
        RichLinkView richLinkView;
        ProgressBar progressBar;

        public ViewHolderL(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            imageView2 = itemView.findViewById(R.id.imageView2);
            richLinkView = itemView.findViewById(R.id.richLinkView);
            progressBar = itemView.findViewById(R.id.progress_circular);
            imageView2.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    static class ViewHolderR extends RecyclerView.ViewHolder {

        TextView textView, textView2, textView3;
        ImageView imageView;
        RichLinkView richLinkView;
        ProgressBar progressBar;

        public ViewHolderR(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
            richLinkView = itemView.findViewById(R.id.richLinkView);
            progressBar = itemView.findViewById(R.id.progress_circular);
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void settingsDialog(String URL, int flag) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        ImageView i = dialog.findViewById(R.id.imageView3);
        ImageView i2 = dialog.findViewById(R.id.imageView4);
        WebView web_view = dialog.findViewById(R.id.web_view);
        web_view.setVisibility(View.GONE);
        i2.setVisibility(View.GONE);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                web_view.destroy();
            }
        });
        if (flag == 0) {
            i2.setVisibility(View.VISIBLE);
            if (TwilioChatApplication.get() != null)
                Glide.with(TwilioChatApplication.get()).load(URL).into(i2);
        } else if (flag == 1) {
            web_view.setVisibility(View.VISIBLE);
            web_view.loadUrl(URL);
        }
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void browserIntent(String URL) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(URL));
            TwilioChatApplication.get().startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}