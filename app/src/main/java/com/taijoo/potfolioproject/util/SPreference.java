package com.taijoo.potfolioproject.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**#################################################################################################
 *                                      <SPreference 클래스>
 *
 * 설명 : SharedPreference 생성, 데이터 넣기, 가져오기
 *
 * 사용법 :
 *        1. SharedPreference 를 사용할 클래스에서 SPreference pref 객체 선언
 *        2. pref = new SPreference(context)로 객체 생성
 *        ***2,1 특정 sharedPreference 파일 생성하여 사용시
 *               pref = new SPreference(context,파일이름,모드)
 *               MODE_PRIVATE = 0
 *        3. 데이터 저장 사용방법 : pref.putValue(key,value);
 *        4. 데이터 가져오기 사용방법 : pref.getIntValue(key);
 *           (자신이 가져올 데이터의 타입에 맞는 메소드 사용)
 *        5. 데이터 지우기 : pref.remove(key); 키에 해당하는 key,value 모두 지움.
 *
 * 사용위치 :
 * 세부사항 : 각 설명의 세부사항 참고
 * 순서 : 생성자 1 (기본 쉐어드를 사용하는 경우)
 *        생성자 2 (특정 이름을 가진 쉐어드를 만들어 사용하는 경우)
 *        putValue(String, int, float, boolean 순서)메소드들
 *        getStringValue()
 *        getIntValue()
 *        getFloatValue()
 *        getBooleanValue()
 *        remove()

 *-----------------------------------------------
 *    수정일        수정자       수정내용
 *-----------------------------------------------
 * 2018.07.18       Jinha        최초생성
 *#################################################################################################*/

public class SPreference {

    private SharedPreferences pref;                                                                 // 사용할 쉐어드 객체
    private SharedPreferences.Editor editor;                                                        // 사용하는 쉐어드 객체의 editor
    private final int NONUMBER = -11;                                                               // float, int 형의 디폴트 값
    private final static String TAG = "SPreference";                                                // SPreference 클래스 로그 태그


    /** #############################################
     *                   생 성 자1
     *
     * 설명 : 앱에 있는 기본 쉐어드를 사용할 때 쓰는 생성자
     *
     * 사용위치 :
     *
     * 매개변수 : context( context )
     *
     * 세부사항 :
     *-----------------------------------------------
     *    수정일        수정자           수정내용
     *-----------------------------------------------
     * 2018.07.18       Jinha            최초생성
     * ################################################
     */

    public SPreference(Context context) {
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);                         // 앱에서 사용하는 기본 Shared 객체
        this.editor = pref.edit();                                                                  // 가져온 Shared 객체의 Editor
    }


    /**
     * #############################################
     * 생 성 자2
     *
     * 설명 : 특정 이름을 가진 쉐어드를 사용할 때 쓰는 생성자.
     *
     * 사용위치 :
     *
     * 매개변수 : context( context ),
     * String prefName (사용하는 쉐어드의 파일 이름),
     * int mode (사용하는 쉐어드의 모드, MODE_PRIVATE =0. MODE_WORLD_READABLE =1, MODE_WORLD_WRITABLE =2)
     *
     * 세부사항 :
     * -----------------------------------------------
     * 수정일        수정자           수정내용
     * -----------------------------------------------
     * 2018.07.18    Jinha            최초생성
     * ################################################
     */

    public SPreference(Context context, String prefName, int mode) {
        this.pref = context.getSharedPreferences(prefName, mode);
        this.editor = pref.edit();
    }


    /**
     * #############################################
     * putValue : 쉐어드에 데이터를 저장하는 함수
     *
     * 설명 : 쉐어드에 데이터를 저장
     *
     * 사용위치 :
     *
     * 매개변수 : key ( 저장할 데이터의 key 값) , value( 저장할 데이터 )
     *
     * 세부사항 : String, int, float, boolean 각각 메소드 있음.
     * 객체를 만든 클래스에서 객체명.putValue(키,값) 형태로 사용
     *
     * -----------------------------------------------
     * 수정일        수정자           수정내용
     * -----------------------------------------------
     * 2018.07.18       Jinha         최초생성
     * ################################################
     */

    public void putValue(String key, String value) {

        if (pref != null) {
            editor.putString(key, value);
            editor.commit();
        } else {
        }

    }

    public void putValue(String key, int value) {
        if (pref != null) {
            editor.putInt(key, value);
            editor.commit();
        } else {
        }

    }

    public void putValue(String key, float value) {
        if (pref != null) {
            editor.putFloat(key, value);
            editor.commit();
        } else {
        }
    }

    public void putValue(String key, boolean value) {
        if (pref != null) {
            editor.putBoolean(key, value);
            editor.commit();
        } else {
        }
    }


    /**
     * #############################################
     * get(Data Type) Value : 해당 키를 가진 데이터를 쉐어드에서 가져오는 함수
     *
     * 설명 : 쉐어드에 저장되어 있는 데이터를 가져옴,
     *
     * 사용위치 :
     *
     * 매개변수 : key ( 가져올 데이터의 key값 )
     *
     * 리턴 값 : key에 해당하는 데이터
     * * 디폴트 리턴 값 (쉐어드에 저장된 데이터가 없을 경우)
     * String : null
     * int, float : NONUMBER(-11)
     * ** 쉐어드에 int, float 형 데이터 저장시 value가 -11이 되는 경우는 피해 주시기 바랍니다.
     * boolean : false
     *
     * 세부사항 : String, int, float, boolean 각각 메소드 있음.
     * 객체를 만든 클래스에서 객체명.get(가져올 데이터 타입)Value(키) 형태로 사용된다.
     * 예 : String name = pref.getStringValue("name");
     *
     * -----------------------------------------------
     * 수정일        수정자           수정내용
     * -----------------------------------------------
     * 2018.07.18       Jinha        최초생성
     * ################################################
     */

    public String getStringValue(String key) {
        String val;

        if (pref != null) {
            val = pref.getString(key, null);
            if (val != null) {
                return val;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public int getIntValue(String key) {
        int val;

        if (pref != null) {
            val = pref.getInt(key, NONUMBER);
            if (val != NONUMBER) {
                return val;
            } else {
                return NONUMBER;
            }
        } else {
            return NONUMBER;
        }
    }

    public float getFloatValue(String key) {
        float val;

        if (pref != null) {
            val = pref.getFloat(key, (float) NONUMBER);
            if (val != (float) NONUMBER) {
                return val;
            } else {
                return NONUMBER;
            }
        } else {
            return NONUMBER;
        }
    }

    public boolean getBooleanValue(String key) {

        if (pref != null) {
            return pref.getBoolean(key, false);
        } else {
            return false;
        }
    }

    /**
     * #############################################
     * remove
     *
     * 설명 : : 파라메터로 보낸 키와 일치하는 키,벨류를 쉐어드에서 삭제 하는 함수
     *
     * 사용위치 :
     *
     * 매개변수 : key ( 쉐어드에서 삭제할 key )
     *
     * 세부사항 : 객체명.remove(삭제할 데이터의 키) 형태로 사용된다.
     * -----------------------------------------------
     * 수정일        수정자           수정내용
     * -----------------------------------------------
     * 2018.07.18    Jinha            최초생성
     * ################################################
     */

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
