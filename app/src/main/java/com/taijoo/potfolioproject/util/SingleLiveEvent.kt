package com.taijoo.potfolioproject.util

/*
* 저작권 2017 Google Inc.
*
* Apache 라이센스 버전 2.0(이하 "라이선스")에 따라 라이센스 부여됨;
* 라이선스를 준수하지 않는 한 이 파일을 사용할 수 없습니다.
* 다음 위치에서 라이센스 사본을 얻을 수 있습니다.
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* 해당 법률에서 요구하거나 서면으로 동의하지 않는 한 소프트웨어
* 라이센스에 따라 배포되는 것은 "있는 그대로" 배포됩니다.
* 명시적이든 암시적이든 어떤 종류의 보증이나 조건도 없습니다.
* 사용 권한에 대한 특정 언어는 사용권을 참조하십시오.
* 라이센스에 따른 제한 사항.
*/

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 가입 후 새로운 업데이트만 전송하는 라이프사이클 인식 관찰 가능, 다음과 같은 이벤트에 사용됩니다.
 * 내비게이션 및 스낵바 메시지.
 *
 *
 * 이렇게 하면 이벤트와 관련된 일반적인 문제를 방지할 수 있습니다. 구성 변경 시(예: 교체) 업데이트
 * 관찰자가 활성 상태인 경우 방출할 수 있습니다. 이 LiveData는 관찰 가능한 데이터가 있는 경우에만
 * setValue() 또는 call()을 명시적으로 호출합니다.
 *
 *
 * 변경 사항은 한 명의 관찰자에게만 통보됩니다.
 */

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
    companion object {
        private val TAG = "SingleLiveEvent"
    }
}