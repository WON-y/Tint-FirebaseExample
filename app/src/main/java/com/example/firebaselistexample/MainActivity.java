package com.example.firebaselistexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview); //연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //Post 객체를 담을 ArrayList (어뎁터 쪽으로)

        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Post"); //DB테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //파이어베이스에서 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트가 존재하지 않도록 초기화
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){ //반복문으로 데이터리스트 추출해냄
                    Post post = dataSnapshot.getValue(Post.class); //만들어뒀던 Post 객체의 데이터를 담음
                    arrayList.add(post); //담은 데이터를 배열리스트에 넣고 리사이클러뷰에 넣을 준비를 함
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오던 중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); //에러문 출력
            }
        });

        adapter = new PostAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

    }
}