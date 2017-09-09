package mapper;

import com.weapes.bookcrawler.mapper.BookMapper;
import com.weapes.bookcrawler.store.Book;
import com.weapes.bookcrawler.util.SqlHelper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * Created by 不一样的天空 on 2017/6/10.
 */
public class MapperTest {
    @Test
    public void mapperTest(){
        SqlSession sqlSession= SqlHelper.openSqlSession();
        BookMapper bookMapper=sqlSession.getMapper(BookMapper.class);
        Book book=new Book()
                .setTitle("解忧杂货店")
                .setType("小说");
        bookMapper.saveBook(book);
        sqlSession.commit();
        sqlSession.close();
    }
}
