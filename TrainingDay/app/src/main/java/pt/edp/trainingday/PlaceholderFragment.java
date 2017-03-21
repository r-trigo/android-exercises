package pt.edp.trainingday;

/**
 * Created by e348900 on 23-02-2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_swiper, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        //next
        ImageView next = (ImageView) rootView.findViewById(R.id.imageView_next);
        next.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigate_next_black_24dp));
        //before
        ImageView before = (ImageView) rootView.findViewById(R.id.imageView_before);
        before.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigate_before_black_24dp));

        if (getArguments().getInt(ARG_SECTION_NUMBER) < 2) {
            before.setVisibility(View.INVISIBLE);
        } else if (getArguments().getInt(ARG_SECTION_NUMBER) > 4) {
            next.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }
}