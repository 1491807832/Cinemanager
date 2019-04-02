package net.lzzy.cinemanager.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment {


    private List<Order> orders;
    private OrderFactory factory;
    private GenericAdapter<Order> adapter;
    private Order order;
    public OrdersFragment() {
    }

    public OrdersFragment(Order order) {
        this.order = order;
    }

    public void save(Order order) {
        adapter.add(order);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        adapter.notifyDataSetChanged();
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void Populate() {
        ListView lv=find(R.id.activity_order_lv);
        View empty=find(R.id.activity_order_tv_none);
        lv.setEmptyView(empty);
        factory=OrderFactory.getInstance();
        orders=factory.get();
        adapter=new GenericAdapter<Order>(getActivity(),R.layout.cinema_item,orders) {
            @Override
            public void populate(ViewHolder holder, Order order) {
                String location = CinemaFactory.getInstance()
                        .getById(order.getCinemaId().toString()).toString();
                holder.setTextView(R.id.activity_cinema_item_name, order.getMovie())
                        .setTextView(R.id.activity_cinema_item_area, location);
            }

            @Override
            public boolean persistInsert(Order order) {
                return factory.addOrder(order);
            }

            @Override
            public boolean persistDelete(Order order) {
                return factory.delete(order);
            }
        };
        lv.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_orders;
    }

    @Override
    public void sarch(String kw) {
        orders.clear();
        if (TextUtils.isEmpty(kw)){
            orders.addAll(factory.get());
        }else {
            orders.addAll(factory.searchOrders(kw));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void svae(Cinema cinema) {

    }
}


