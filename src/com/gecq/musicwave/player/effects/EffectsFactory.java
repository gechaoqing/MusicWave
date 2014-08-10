package com.gecq.musicwave.player.effects;

import android.media.audiofx.EnvironmentalReverb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoqing on 14-8-2.
 */
public class EffectsFactory {
    private static List<Effect> preset=new ArrayList<Effect>();
    public static List<Effect> getEffects(){
        if(preset.size()==0){
            add("无音效","none",Effect.REVERB_NONE);
            add("通用音效","generic",Effect.REVERB_GENERIC);
            add("小巷","alley",Effect.REVERB_ALLEY);
            add("竞技场","arena",Effect.REVERB_ARENA);
            add("礼堂","auditorium",Effect.REVERB_AUDITORIUM);
            add("浴室","bathroom",Effect.REVERB_BATHROOM);
            add("地毯走廊","carpeted_hallway",Effect.REVERB_CARPETEDHALLWAY);
            add("洞穴","cave",Effect.REVERB_CAVE);
            add("城市","city",Effect.REVERB_CITY);
            add("音乐厅","concert_hall",Effect.REVERB_CONCERTHALL);
            add("森林","forest",Effect.REVERB_FOREST);
            add("普通走廊","hallway",Effect.REVERB_HALLWAY);
            add("飞机棚","hangar",Effect.REVERB_HANGAR);
            add("大厅","large_hall",Effect.REVERB_LARGEHALL);
            add("大房间","large_room",Effect.REVERB_LARGEROOM);
            add("客厅","living_room",Effect.REVERB_LIVINGROOM);
            add("中厅","medium_hall",Effect.REVERB_MEDIUMHALL);
            add("中房间","medium_room",Effect.REVERB_MEDIUMROOM);
            add("群山","mountains",Effect.REVERB_MOUNTAINS);
            add("软垫小屋","padded_cell",Effect.REVERB_PADDEDCELL);
            add("停车场","parking_lot",Effect.REVERB_PARKINGLOT);
            add("平原","plain",Effect.REVERB_PLAIN);
            add("盆地","plate",Effect.REVERB_PLATE);
            add("采石场","quarry",Effect.REVERB_QUARRY);
            add("普通房间","room",Effect.REVERB_ROOM);
            add("下水道","sewer_pipe",Effect.REVERB_SEWERPIPE);
            add("小房间","small_room",Effect.REVERB_SMALLROOM);
            add("石头走廊","stone_corridor",Effect.REVERB_STONECORRIDOR);
            add("石屋","stone_room",Effect.REVERB_STONEROOM);
            add("水下","under_water",Effect.REVERB_UNDERWATER);
        }
        return preset;
    }

    private static void add(String name,String pic,EnvironmentalReverb.Settings settings){
        Effect e=new Effect(name,pic,settings);
        preset.add(e);
    }
}
