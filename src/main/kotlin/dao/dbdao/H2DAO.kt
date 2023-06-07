package bingo.inputoutput.dao.DB

import dao.DAO
import javax.sql.DataSource



abstract class DBDAO<T>(ds: DataSource): DAO<T>


