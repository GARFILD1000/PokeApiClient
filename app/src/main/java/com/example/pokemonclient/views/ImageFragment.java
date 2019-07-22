package com.example.pokemonclient.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pokemonclient.R;
import com.squareup.picasso.Picasso;

//fragment, that contains single image
public class ImageFragment extends Fragment {
    ImageView imageView;
    String imageUrl = "";
    private static final String BUNDLE_IMAGE_URL= "imagefragment.imageurl";

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        this.imageUrl = getArguments().getString(BUNDLE_IMAGE_URL);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = view.findViewById(R.id.imageView);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(imageView);
        }
        else{
            //if there is no image in fragment, loads image that show it
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.no_image,null));
        }
        return view;
    }

    //creating fragment with loading parameters through bundle
    public static ImageFragment newInstance(String imageUrl){
        ImageFragment imageFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_IMAGE_URL, imageUrl);
        imageFragment.setArguments(args);
        return imageFragment;
    }
}
