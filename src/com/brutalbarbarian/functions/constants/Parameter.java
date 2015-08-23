package com.brutalbarbarian.functions.constants;

import javafx.stage.FileChooser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Lu on 3/13/14.
 */
public enum Parameter {
    FirstNumber,
    SecondNumber,
    ThirdNumber,
    UpperLimit,
    Target, Digits, LowerLimit,

    PageCount,
    Subreddit,

    VideoPath,
    LabelRefPath,
    Duration;

    public boolean isPath() {
        return (this == VideoPath) ||
                (this == LabelRefPath);
    }

    public void setupExtensions(List<FileChooser.ExtensionFilter> extensionFilters) {
        if (this == VideoPath) {
            extensionFilters.add(new FileChooser.ExtensionFilter("MP4", Arrays.asList("*.mp4", "*.m4v")));
        } else if (this == LabelRefPath) {
            extensionFilters.add(new FileChooser.ExtensionFilter("PNG", "*.png"));
            extensionFilters.add(new FileChooser.ExtensionFilter("JPEG", Arrays.asList("*.jpeg", "*.jpg")));
            extensionFilters.add(new FileChooser.ExtensionFilter("BMP", "*.bmp"));
        }
    }
}
