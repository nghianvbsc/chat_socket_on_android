package cf.bscenter.demochatsocketandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nghianv on 01/08/2016
 */

class MessageAdapter extends BaseAdapter {
    private List<String> mMessageList;
    private Context mContext;

    MessageAdapter(List<String> messageList, Context context) {
        mMessageList = messageList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mMessageList == null ? 0 : mMessageList.size();
    }

    @Override
    public String getItem(int index) {
        return mMessageList.get(index);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        View newView = view;
        ViewHolder viewHolder;
        if (newView == null) {
            newView = LayoutInflater.from(mContext).inflate(R.layout.item_lv_message, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvMessage = (TextView) newView.findViewById(R.id.tvMessage);
            newView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvMessage.setText(getItem(index));
        return newView;
    }

    private class ViewHolder {
        private TextView tvMessage;
    }
}
