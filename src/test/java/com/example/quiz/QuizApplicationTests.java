package com.example.quiz;

import com.example.quiz.dao.FeedbackDao;
import com.example.quiz.dao.QuizDao;
import com.example.quiz.entity.Question;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class QuizApplicationTests {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuizService quizService;

	@Autowired
	private FeedbackDao feedbackDao;

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(List.of("AAA", "BBB", "CCC"));
		List<Question> list = new ArrayList<>();
		list.add(new Question(1,1,"A","Single",true, str));
		LocalDate date = LocalDate.of(2025, 3, 27);
		LocalDate endDate = LocalDate.of(2025, 3, 28);
		CreateReq req = new CreateReq(1,"AAA","zcx",date,endDate,true,list);
		BasicRes res = quizService.create(req);
		System.out.println(res.getCode() + res.getMessage());
	}

	@Test
	public void searchTest(){
		SearchReq req = new SearchReq(null,null,null);
		SearchRes res = quizService.getAll(req);
		System.out.println(res.getQuizList().size());
	}
	@Test
	public void listTest(){
		List<String> list = new ArrayList<>();
		List<String> srtList = List.of("1:aa","2:bb", "3:cc");
		for(String item : srtList){
			String[] strArray = item.split(":");
			list.addAll(List.of(strArray));
		}

		List<String> srtList1 = List.of("aa","bb", "cc");
		for(String item : srtList1){
			String[] strArray = item.split(":");
			list.addAll(List.of(strArray));
		}
		System.out.println(list.size());
	}
	@Test
	public void joinTest(){
		List<FeedbackDto> res = feedbackDao.selectFeedback(1);
		System.out.println(res.size());
	}
}
