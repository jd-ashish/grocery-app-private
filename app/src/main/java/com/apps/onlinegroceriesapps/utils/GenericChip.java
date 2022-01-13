package com.apps.onlinegroceriesapps.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenericChip {

    private static GenericChipInterface.CallBack mCallBackChipReset;

    public GenericChip() {

    }

    public interface GenericChipInterface {
        interface CallBack {
            void onResetChip(String type);
        }
    }

    public GenericChip(GenericChipInterface.CallBack mCallBackChipReset) {
        this.mCallBackChipReset = mCallBackChipReset;
    }

    public static void ClearSelectedChip(ChipGroup chipGroup,Context context) {
        ChipEnables(chipGroup,context,false);
    }
    public static void ChipEnables(ChipGroup chipGroup,Context context,boolean isChipEnable) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setChecked(isChipEnable);
        }
    }
    public static List<String> getSelectedChipList(ChipGroup chipGroup) {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selected.add((String) chip.getText());
            }
        }
        return selected;
    }
    //TODO TRY to remove this part.
    public static String getSelectedChipString(List<String> list) {
        StringBuilder data;
        if (!list.isEmpty()) {
            data = new StringBuilder(Util.lowerFirstStr(list.get(0)));
            for (int i = 1; i < list.size(); i++)
                data.append(",").append(Util.lowerFirstStr(list.get(i)));
        } else
            data = null;
        return String.valueOf(data==null?"":data);
    }


    public static void removeAllChip(ChipGroup chip) {
        List<Integer> a = chip.getCheckedChipIds();
        for (int b : a) {
            Chip c = chip.findViewById(b);
            chip.removeView(c);
        }
    }
    public void removeChipByText(ChipGroup chip, String title) {
        List<Integer> a = chip.getCheckedChipIds();
        for (int b : a) {
            Chip c = chip.findViewById( b );
            if (c.getText().equals( title ))
                chip.removeView( c );
        }
    }
}
