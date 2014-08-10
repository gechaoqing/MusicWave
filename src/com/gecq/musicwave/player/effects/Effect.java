package com.gecq.musicwave.player.effects;

import android.media.audiofx.EnvironmentalReverb.Settings;

/**
 * Created by chaoqing on 14-8-2.
 */
public class Effect {
    private String name;
    private Settings settings;
    private String pic;
    public Effect(String name,String pic,Settings settings){
        setName(name);
        setPic(pic);
        setSettings(settings);
    }
    private static int gain=2000;
    public static final Settings REVERB_NONE = new Settings(getReverb(-32767, 0, 1000, 500, -32767, 20, -32767, 40, 1000, 1000));
    public static final Settings REVERB_GENERIC= new Settings(getReverb(0,-100,1490,830,-2602,7,200,11,1000,1000));
    public static final Settings REVERB_PADDEDCELL= new Settings(getReverb(0,-6000,170,100,-1204,1,207,2,1000,1000));
    public static final Settings REVERB_ROOM= new Settings(getReverb(0,-454,400,830,-1646,2,53,3,1000,1000));
    public static final Settings REVERB_BATHROOM= new Settings(getReverb(0,-1200,1490,540,-370,7,1030,11,1000,600));
    public static final Settings REVERB_LIVINGROOM= new Settings(getReverb(0,-6000,500,100,-1376,3,-1104,4,1000,1000));
    public static final Settings REVERB_STONEROOM= new Settings(getReverb(0,-300,2310,640,-711,12,83,17,1000,1000));
    public static final Settings REVERB_AUDITORIUM= new Settings(getReverb(0,-476,4320,590,-789,20,-289,30,1000,1000));
    public static final Settings REVERB_CONCERTHALL= new Settings(getReverb(0,-500,3920,700,-1230,20,-2,29,1000,1000));
    public static final Settings REVERB_CAVE= new Settings(getReverb(0,0,2910,1300,-602,15,-302,22,1000,1000));
    public static final Settings REVERB_ARENA= new Settings(getReverb(0,-698,7240,330,-1166,20,16,30,1000,1000));
    public static final Settings REVERB_HANGAR= new Settings(getReverb(0,-1000,10050,230,-602,20,198,30,1000,1000));
    public static final Settings REVERB_CARPETEDHALLWAY= new Settings(getReverb(0,-4000,300,100,-1831,2,-1630,30,1000,1000));
    public static final Settings REVERB_HALLWAY= new Settings(getReverb(0,-300,1490,590,-1219,7,441,11,1000,1000));
    public static final Settings REVERB_STONECORRIDOR= new Settings(getReverb(0,-237,2700,790,-1214,13,395,20,1000,1000));
    public static final Settings REVERB_ALLEY= new Settings(getReverb(0,-270,1490,860,-1204,7,-4,11,1000,1000));
    public static final Settings REVERB_FOREST= new Settings(getReverb(0,-3300,1490,540,-2560,162,-613,88,790,1000));
    public static final Settings REVERB_CITY= new Settings(getReverb(0,-800,1490,670,-2273,7,-2217,11,500,1000));
    public static final Settings REVERB_MOUNTAINS= new Settings(getReverb(0,-2500,1490,210,-2780,300,-2014,100,270,1000));

    public static final Settings REVERB_QUARRY= new Settings(getReverb(0,-1000,1490,830,-32767,61,500,25,1000,1000));
    public static final Settings REVERB_PLAIN= new Settings(getReverb(0,-2000,1490,500,-2466,179,-2514,100,210,1000));
    public static final Settings REVERB_PARKINGLOT= new Settings(getReverb(0,0,1650,1500,-1363,8,-1153,12,1000,1000));
    public static final Settings REVERB_SEWERPIPE= new Settings(getReverb(0,-1000,2810,140,429,14,648,21,800,600));
    public static final Settings REVERB_UNDERWATER= new Settings(getReverb(0,-4000,1490,100,-449,7,1700,11,1000,1000));
    public static final Settings REVERB_SMALLROOM= new Settings(getReverb(0,-600,1100,830,-400,5,500,10,1000,1000));
    public static final Settings REVERB_MEDIUMROOM= new Settings(getReverb(0,-600,1300,830,-1000,20,-200,20,1000,1000));
    public static final Settings REVERB_LARGEROOM= new Settings(getReverb(0,-600,1500,830,-1600,5,-1000,40,1000,1000));
    public static final Settings REVERB_MEDIUMHALL= new Settings(getReverb(0,-600,1800,700,-1300,15,-800,30,1000,1000));
    public static final Settings REVERB_LARGEHALL= new Settings(getReverb(0,-600,1800,700,-2000,30,-1400,60,1000,1000));
    public static final Settings REVERB_PLATE= new Settings(getReverb(0,-200,1300,900,0,2,0,10,1000,750));

    public static String getReverb(int rl, int rhl, int det, int dehf, int refl, int refd, int reverl, int reverd, int dif, int densi) {
        reverl+=gain;
        if(reverl>2000){
        	reverl=2000;
        }
    	return "EnvironmentalReverb" +
                ";roomLevel=" + rl +
                ";roomHFLevel=" + rhl +
                ";decayTime=" + det +
                ";decayHFRatio=" + dehf +
                ";reflectionsLevel=" + refl +
                ";reflectionsDelay=" + refd +
                ";reverbLevel=" + reverl +
                ";reverbDelay=" + reverd +
                ";diffusion=" + dif +
                ";density=" + densi;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
