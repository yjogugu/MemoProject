package com.taijoo.potfolioproject.presentation.view.memo


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.taijoo.potfolioproject.data.repository.Http.Repository
import com.taijoo.potfolioproject.data.repository.room.Dao.MemoDao
import com.taijoo.potfolioproject.data.repository.room.Dao.UserDao
import com.taijoo.potfolioproject.data.repository.room.Database.UserDB
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.data.repository.room.Repository.MemoRepository
import com.taijoo.potfolioproject.data.repository.room.Repository.UserRepository
import com.taijoo.potfolioproject.data.repository.room.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MemoViewModel(application: Application) : ViewModel() {

    private val userDatabase = UserDB.getInstance(application)!!
    private val memoDao: MemoDao = userDatabase.memoDao()
    private val userDao: UserDao = userDatabase.userDao()

    private val server_api : Repository = Repository()

    private val memo_api : MemoRepository = MemoRepository(memoDao)
    private val user_api : UserRepository = UserRepository(userDao)

    var adapter_position = 0//어뎁터 포지션

    var type = 0// 0 기본 , 1 데이터 추가 , 2 데이터 삭제 , 3 데이터 수정

    var _check_count = MutableLiveData<Int>()//삭제 체크박스 갯수
    val check_count: LiveData<Int> get() = _check_count

    private var _delete_string = MutableLiveData<String>()//삭제 완료 버튼 텍스트
    val delete_string: LiveData<String> get() = _delete_string

    var delete_item = ArrayList<Int>()//삭제 완료 버튼 텍스트

    var userData :LiveData<User> = user_api.getAll()

    lateinit var memoData : LiveData<List<Memo>>

    var networkState = false

    init {
        _check_count.value = 0
        _delete_string.value = "취소"
    }

    fun getServerUser(){
        server_api.getUserInfo(this)
    }

    fun updateToken(userSeq : Int , token : String){
        viewModelScope.launch {
            server_api.updateToken("TokenUpdate",userSeq, token)
        }

    }

    fun setUser(user: User){
        viewModelScope.launch {
            userDao.insertUserAll(user)
        }
    }

    //삭제 체크박스 갯수 표시시
   fun getCheckCount(isCheck : Boolean , position : Int ){
        if(isCheck){
            _check_count.value =(_check_count.value)?.plus(1)
            delete_item.add(position)
        }
        else{
            _check_count.value = (_check_count.value)?.minus(1)
            delete_item.remove(position)
        }

        getDelete_string(_check_count.value!!)
    }

    //삭제 버튼 텍스트 변경
    fun getDelete_string(count : Int){
        if(count == 0){
            _delete_string.value = "취소"
        }
        else{
            _delete_string.value = "완료"
        }
    }

    fun getContent(): Flow<PagingData<Memo>> {

        return memo_api.getMemo().cachedIn(viewModelScope)

    }

    //Memo 데이터 Flow 방식으로 데이터 갖고오기 페이징 기법
    fun getContent(start: Int) : Flow<List<Memo>> {

        return memo_api.getMemoDataPaging(start).flowOn(Dispatchers.Default).conflate()
    }

    fun getContentMainThread(start: Int) : List<Memo> {

        return memo_api.getMemoDataPagingMainThread(start)
    }


    //Memo,Icon 데이터 로컬DB에 저장
    fun setMemoData(icon_color_position: Int, icon_position: Int, title: String, content: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

                val date = Date()
                val strDate: String = dateFormat.format(date)
                memo_api.insertMemo(Memo(0, icon_color_position, icon_position, 0, title, content, strDate))

            }
        }

    }

    //메모장 위치 업데이트
    fun updateMemoData(icon_position: Int, icon_seq: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                memo_api.updateMemo(icon_position, icon_seq)
            }

        }
    }

    //메모장 수정 업데이트
    fun updateMemoData(icon_seq: Long , icon_color_position : Int , memo_title : String , memo_content : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                memo_api.updateMemo(icon_seq, icon_color_position, memo_title, memo_content)
            }

        }
    }

    fun deleteMemoData(icon_seq: Long){
        viewModelScope.launch {
            memo_api.deleteMemo(icon_seq)
        }
    }

    //메모장 데이터 Room 로컬에서 가져오기
    fun getMemoData(start: Int, end: Int):LiveData<List<Memo>> {
        memoData = memo_api.selectMemo(start, end)

        return memoData
    }



    fun setDataType(type : Int){
        this.type = type
    }

    fun getDataType(): Int{

        return this.type
    }


}