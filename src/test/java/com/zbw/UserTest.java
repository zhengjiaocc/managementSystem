package com.zbw;

import com.zbw.domain.BorrowingBooks;
import com.zbw.domain.BorrowingBooksExample;
import com.zbw.domain.Department;
import com.zbw.domain.User;
import com.zbw.domain.Vo.BorrowingBooksVo;
import com.zbw.mapper.BorrowingBooksMapper;
import com.zbw.mapper.UserMapper;
import com.zbw.service.IBorrowingBooksRecordService;
import com.zbw.service.IUserService;
import com.zbw.utils.page.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private IBorrowingBooksRecordService borrowingBooksRecordService;
    @Resource
    private HttpServletRequest request;

    @Resource
    private BorrowingBooksMapper borrowingBooksMapper;

    @Test
    public void findUsersByName() {
        List<User> users = userService.findUserByUserName("yxc");
        if (null != users) {
            for (User u : users) {
                System.out.println(u.getUserName());
            }
        } else {
            System.out.println("null");
        }
    }

    @Test
    public void findAllDepts() {
        List<Department> depts = userService.findAllDepts();
        if (null == depts) {
            System.out.println("null");
        } else {
            for (Department d : depts) {
                System.out.println(d.getDeptName());
            }
        }
    }

    @Test
    public void updateUserTest() {
        User user = new User();

    }

    @Test
    public void selectByPage() {
        List<User> users = userMapper.selectByPageNum(0, 5);
        if (null != users) {
            for (User u : users) {
                System.out.println(u.getUserId() + " " + u.getUserName());
            }
        }
    }

    @Test
    public void tsetSelcetCount() {
        int n = userMapper.selectUserCount();
        System.out.println(n);
    }

    @Test
    public void testSelectAllBorrowingBooksByPageNum() {
        Page<BorrowingBooksVo> page = borrowingBooksRecordService.selectAllByPage(1);
        if (null != page) {
            for (BorrowingBooksVo b : page.getList()) {
                System.out.println(b.getUser().getUserName() + " " + b.getBook().getBookName());
            }
        }
    }

    @Test
    public void testAddUser() {
        User u1 = new User();
        u1.setUserId(null);
        u1.setUserName("zhangshan");
        u1.setUserEmail("zhangshan@163.com");
        u1.setUserPwd("123456");
        userService.insertUser(u1);

    }

    @Test
    public void userBorrowingBook() {
        //检查书籍是否已被借出【查借阅记录】：
        BorrowingBooksExample borrowingBooksExample = new BorrowingBooksExample();
        BorrowingBooksExample.Criteria criteria = borrowingBooksExample.createCriteria();
        criteria.andBookIdEqualTo(71);
        List<BorrowingBooks> list = borrowingBooksMapper.selectByExample(borrowingBooksExample);
        if (!list.isEmpty()) {
            System.out.println("失败");
            return;
        }
        //创建新的借书记录
        BorrowingBooks borrowingBooks = new BorrowingBooks();
        borrowingBooks.setBookId(71);
        borrowingBooks.setUserId(7);
        borrowingBooks.setDate(new Date());

        //尝试插入借书记录：
        try {
            //数据库中增加一条借书记录 【如果插入失败 , 则借书失败】
            borrowingBooksMapper.insert(borrowingBooks);
        } catch (Exception e) {
            System.out.println("失败");
        }

    }
}
