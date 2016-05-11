package com.abubusoft.kripton.example02;

import java.util.List;

import com.abubusoft.kripton.android.annotation.BindDaoDefinition;
import com.abubusoft.kripton.android.annotation.BindSelect;

/**
 * Created by 908099 on 10/05/2016.
 */
@BindDaoDefinition(ChannelMessage.class)
public interface DaoChannelMessage {

    @BindSelect
    List<ChannelMessage> selectAll();
}