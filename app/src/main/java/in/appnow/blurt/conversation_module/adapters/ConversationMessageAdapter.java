package in.appnow.blurt.conversation_module.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.appnow.blurt.R;
import in.appnow.blurt.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.blurt.conversation_module.view_holders.ChatItemViewHolder;
import in.appnow.blurt.helper.PreferenceManger;


public class ConversationMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ConversationMessageAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SELF = 1;
    private static final int TYPE_OTHER = 2;
    private static final int TYPE_SELF_IMAGE = 3;
    private static final int TYPE_OTHER_IMAGE = 4;
    private static final int TYPE_SELF_VIDEO = 5;
    private static final int TYPE_OTHER_VIDEO = 6;

    private Context mContext;
    private List<ConversationResponse> conversationModelArrayList = new ArrayList<>(0);


    private PreferenceManger preferenceManger;

    public ConversationMessageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPreferenceManger(PreferenceManger preferenceManger) {
        this.preferenceManger = preferenceManger;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_SELF) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
            return new ChatItemViewHolder(mContext, itemView);
        } /*else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_load_more_view, parent, false);
            return new LoadMoreViewHolder(itemView);
        } */ else if (viewType == TYPE_OTHER) {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
            return new ChatItemViewHolder(mContext, itemView);
        }


        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public int getItemViewType(int position) {
        ConversationResponse conversationModel = conversationModelArrayList.get(position);
      /*  if (isPositionHeader(position))
            return TYPE_HEADER;
        else */
       /* if (conversationModel.getIsMessageMine() == ConversationUtils.MESSAGE_MINE)
            return TYPE_SELF;
        else if (conversationModel.getIsMessageMine() == ConversationUtils.MESSAGE_OTHER)
            return TYPE_OTHER;

        return position;*/
        if (conversationModel.getMessageId() == 1)
            return TYPE_OTHER;
        else if (preferenceManger.getUserDetails().getUserId().equalsIgnoreCase(String.valueOf(conversationModel.getSenderId())) )
            return TYPE_SELF;
        else
            return TYPE_OTHER;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatItemViewHolder) {
            ConversationResponse conversationModel = conversationModelArrayList.get(position);
            ((ChatItemViewHolder) holder).bindData(conversationModel, preferenceManger.getUserDetails().getUserId().equalsIgnoreCase(String.valueOf(conversationModel.getSenderId())));
        }
    }


    @Override
    public int getItemCount() {
        return conversationModelArrayList != null ? conversationModelArrayList.size() : 0;
    }


    public void swapData(List<ConversationResponse> conversationResponseList) {
        this.conversationModelArrayList.clear();
        if (conversationResponseList != null && !conversationResponseList.isEmpty()) {
            this.conversationModelArrayList.addAll(conversationResponseList);
        }
        notifyDataSetChanged();
    }

    public List<ConversationResponse> getConversationModelArrayList() {
        return conversationModelArrayList;
    }
}

