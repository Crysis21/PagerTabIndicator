package com.hold1.pagertabsindicator;

import android.net.Uri;
import android.view.View;

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */

public class TabViewProvider {

    public interface ImageProvider {
        Uri getImageUri(int position);
    }
    
    public interface CustomView {
        View getView(int position);
    }

}
