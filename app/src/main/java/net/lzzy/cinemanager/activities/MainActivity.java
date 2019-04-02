package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.AddCinemasFragment;
import net.lzzy.cinemanager.fragments.AddOrdersFragment;
import net.lzzy.cinemanager.fragments.BaseFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OrdersFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.utils.ViewUtils;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , AddCinemasFragment.OnFragmentInteractionListener, AddCinemasFragment.OnCinemaCreatedListener,
        AddOrdersFragment.OnOrderCreatedListener,AddOrdersFragment.OnFragmentInteractionListener {


    private FragmentManager manager = getSupportFragmentManager();
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private SparseArray<String> titleArray = new SparseArray<>();
    private SparseArray<Fragment> fragmentArray = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler(){
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment=manager.findFragmentById(R.id.fragment_container);
                if (fragment!=null){
                    if (fragment instanceof BaseFragment){
                        ((BaseFragment)fragment).sarch(kw);
                    }
                }
                return true;
            }
        });
        manager.beginTransaction().add(R.id.fragment_container,new OrdersFragment()).commit();
    }

    private void setTitleMenu() {
        titleArray.put(R.id.bar_see_cinema, "影院列表");
        titleArray.put(R.id.bar_add_cinema, "添加影院");
        titleArray.put(R.id.bar_add_order, "添加订单");
        titleArray.put(R.id.bar_order, "订单列表");
        layoutMenu = findViewById(R.id.bar_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_img_menu).setOnClickListener(v -> {
            int visible = layoutMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
        tvTitle.setText("我的订单");
        search = findViewById(R.id.bar_searchView);
        findViewById(R.id.bar_order).setOnClickListener(this);
        findViewById(R.id.bar_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_add_order).setOnClickListener(this);
        findViewById(R.id.bar_see_cinema).setOnClickListener(this);
        findViewById(R.id.bar_exit).setOnClickListener(v -> System.exit(0));
    }

    @Override
    public void onClick(View v) {
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArray.get(v.getId()));
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = fragmentArray.get(v.getId());
        if (fragment == null) {
            fragment = createFragment(v.getId());
            fragmentArray.put(v.getId(), fragment);
            transaction.add(R.id.fragment_container, fragment);
        }
        for (Fragment f : manager.getFragments()) {
            transaction.hide(f);
        }
        transaction.show(fragment).commit();

    }

    private Fragment createFragment(int id) {
        switch (id) {
            case R.id.bar_order:
                return new OrdersFragment();
            case R.id.bar_add_order:
                return new AddOrdersFragment();
            case R.id.bar_add_cinema:
                return new AddCinemasFragment();
            case R.id.bar_see_cinema:
                return new CinemasFragment();
            default:
                break;
        }
        return null;
    }



    @Override
    public void cencelAddCinema() {
        Fragment addCinemas=fragmentArray.get(R.id.bar_add_cinema);
        if (addCinemas==null){
            return;
        }
        Fragment cinemas=fragmentArray.get(R.id.bar_see_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemas==null){
            cinemas=new CinemasFragment();
            fragmentArray.put(R.id.bar_see_cinema,cinemas);
            transaction.add(R.id.fragment_container,cinemas);
        }
        transaction.hide(addCinemas).show(cinemas).commit();
        tvTitle.setText(titleArray.get(R.id.bar_see_cinema));
    }

    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemaFragment = fragmentArray.get(R.id.bar_add_cinema);
        if (addCinemaFragment == null) {
            return;
        }
        Fragment cinemasFragment = fragmentArray.get(R.id.bar_see_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (cinemasFragment == null) {
            cinemasFragment = new CinemasFragment(cinema);
        } else {
            ((CinemasFragment) cinemasFragment).svae(cinema);
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_see_cinema));
        search.setVisibility(View.VISIBLE);

    }

    @Override
    public void cancelAddOrder() {
        Fragment addOrders=fragmentArray.get(R.id.bar_add_order);
        if (addOrders==null){
            return;
        }
        Fragment orders=fragmentArray.get(R.id.bar_see_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (orders==null){
            orders=new OrdersFragment();
            fragmentArray.put(R.id.bar_order,orders);
            transaction.add(R.id.fragment_container,orders);
        }
        transaction.hide(addOrders).show(orders).commit();
        tvTitle.setText(titleArray.get(R.id.bar_order));
    }

    @Override
    public void saveOrder(Order order) {
        Fragment addOrders=fragmentArray.get(R.id.bar_add_order);
        if (addOrders==null){
            return;
        }
        Fragment ordersFragment=fragmentArray.get(R.id.bar_order);
        FragmentTransaction transaction=manager.beginTransaction();
        if (ordersFragment==null){
            ordersFragment=new OrdersFragment(order);
            fragmentArray.put(R.id.bar_order,ordersFragment);
            transaction.add(R.id.fragment_container,ordersFragment);
        }else {
            ((OrdersFragment)ordersFragment).save(order);
        }
        transaction.hide(addOrders).show(ordersFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_order));
    }

    @Override
    public void hideSearch() {
        search.setVisibility(View.INVISIBLE);
    }
}


