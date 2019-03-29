package net.lzzy.cinemanager.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddCinemasFragment extends BaseFragment {
    private String city = "柳州市";
    private String province = "广西壮族自治区";
    private String area = "鱼峰区";


    @Override
    protected void Populate() { final TextView tvArea = find(R.id.activity_dialog_location);
        final EditText edtName = find(R.id.activity_dialog_edt_name);
        find(R.id.activity_cinema_content_layoutArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JDCityPicker cityPicker = new JDCityPicker();
                cityPicker.init(AddCinemasFragment.this.getActivity());
                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                        AddCinemasFragment.this.province = province.getName();
                        AddCinemasFragment.this.city = city.getName();
                        AddCinemasFragment.this.area = district.getName();
                        String loc = province.getName() + city.getName() + district.getName();
                        tvArea.setText(loc);
                    }

                    @Override
                    public void onCancel() {
                    }

                });
                cityPicker.showCityPicker();
            }
        });


        find(R.id.activity_dialog_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = edtName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(AddCinemasFragment.this.getActivity(), "要有名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cinema cinema = new Cinema();
                cinema.setCity(city);
                cinema.setName(name);
                cinema.setArea(area);
                cinema.setProvince(province);
                cinema.setLocation(tvArea.getText().toString());

                edtName.setText("");


            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_cincmas;
    }




}
