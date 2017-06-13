package pl.javastart.weekop.dao;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;

import pl.javastart.weekop.model.Discovery;
import pl.javastart.weekop.model.User;
import pl.javastart.weekop.util.ConnectionProvider;
 
public class DiscoveryDAOImpl implements DiscoveryDAO {
 
    private static final String CREATE_DISCOVERY = 
      "INSERT INTO discovery(name, description, url, user_id, date, up_vote, down_vote) "
      + "VALUES(:name, :description, :url, :user_id, :date, :up_vote, :down_vote);";
    private static final String READ_ALL_DISCOVERIES = 
      "SELECT user.user_id, username, email, is_active, password, discovery_id, name, description, url, date, up_vote, down_vote "
      + "FROM discovery LEFT JOIN user ON discovery.user_id=user.user_id;";
    private static final String READ_DISCOVERY = 
        "SELECT user.user_id, username, email, is_active, password, discovery_id, name, description, url, date, up_vote, down_vote "
        + "FROM discovery LEFT JOIN user ON discovery.user_id=user.user_id WHERE discovery_id=:discovery_id;";
    private static final String UPDATE_DISCOVERY = 
        "UPDATE discovery SET name=:name, description=:description, url=:url, user_id=:user_id, date=:date, up_vote=:up_vote, down_vote=:down_vote "
        + "WHERE discovery_id=:discovery_id;";
    private static final String UPDATE_DISCOVERY2 = 
            "UPDATE discovery SET name=:name, description=:description "
            + "WHERE discovery_id=:discovery_id;";
    private static final String TURN_OFF_PK =
    		"SET FOREIGN_KEY_CHECKS=0;";
    private static final String TURN_ON_PK =
    		"SET FOREIGN_KEY_CHECKS=1;";
    private static final String DELETE_DISCOVERY = 
    	"DELETE FROM discovery WHERE discovery_id=?;";
 
    private NamedParameterJdbcTemplate template;
     
    public DiscoveryDAOImpl() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
    }
 
    @Override
    public Discovery create(Discovery discovery) {
        Discovery resultDiscovery = new Discovery(discovery);
        KeyHolder holder = new GeneratedKeyHolder();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", discovery.getName());
        paramMap.put("description", discovery.getDescription());
        paramMap.put("url", discovery.getUrl());
        paramMap.put("user_id", discovery.getUser().getId());
        paramMap.put("date", discovery.getTimestamp());
        paramMap.put("up_vote", discovery.getUpVote());
        paramMap.put("down_vote", discovery.getDownVote());
        SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        int update = template.update(CREATE_DISCOVERY, paramSource, holder);
        if(update > 0) {
            resultDiscovery.setId((Long)holder.getKey());
        }
        return resultDiscovery;
    }
 
    @Override
    public Discovery read(Long primaryKey) {
        SqlParameterSource paramSource = new MapSqlParameterSource("discovery_id", primaryKey);
        Discovery discovery = template.queryForObject(READ_DISCOVERY, paramSource, new DiscoveryRowMapper());
        return discovery;
    }
 
    @Override
    public boolean update(Discovery discovery) {
        boolean result = false;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("discovery_id", discovery.getId());
        paramMap.put("name", discovery.getName());
        paramMap.put("description", discovery.getDescription());
        paramMap.put("url", discovery.getUrl());
        paramMap.put("user_id", discovery.getUser().getId());
        paramMap.put("date", discovery.getTimestamp());
        paramMap.put("up_vote", discovery.getUpVote());
        paramMap.put("down_vote", discovery.getDownVote());
        SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        int update = template.update(UPDATE_DISCOVERY, paramSource);
        if(update > 0) {
            result = true;
        }
        return result;
    }
    
    public boolean update2(String id, String desc, String name){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("discovery_id",id);
        paramMap.put("name",name);
        paramMap.put("description",desc);
        SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        template.update(UPDATE_DISCOVERY2, paramSource);
    	return true;
    }
    
    @Override
    public boolean delete(Long key) { 
    	
    	Connection conn = null;
    	PreparedStatement prepStmt = null;
		try {
			conn = ConnectionProvider.getConnection();
			prepStmt = conn.prepareStatement(TURN_OFF_PK);
			prepStmt.executeUpdate();
			prepStmt.close();
						
		   	prepStmt = conn.prepareStatement(DELETE_DISCOVERY);
	    	prepStmt.setString(1, key.toString());
	    	prepStmt.executeUpdate();
	    	prepStmt.close();
	    	
	    	prepStmt = null;
			prepStmt = conn.prepareStatement(TURN_ON_PK);
			prepStmt.executeUpdate();

	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			releaseResources(prepStmt, null, conn);
		} 
    	
//        SqlParameterSource paramSource = new MapSqlParameterSource("discovery_id", key);
//        template.update(DELETE_DISCOVERY, paramSource); 
        
        return true;
    }
    private void releaseResources(PreparedStatement prepStmt, ResultSet res,
            Connection conn) {
        try {
            if (prepStmt != null && !prepStmt.isClosed()) {
                prepStmt.close();
            }
            if (res != null && !res.isClosed()) {
                res.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public List<Discovery> getAll() {
        List<Discovery> discoveries = template.query(READ_ALL_DISCOVERIES, new DiscoveryRowMapper());
        return discoveries;
    }
     
    private class DiscoveryRowMapper implements RowMapper<Discovery> {
        @Override
        public Discovery mapRow(ResultSet resultSet, int row) throws SQLException {
            Discovery discovery = new Discovery();
            discovery.setId(resultSet.getLong("discovery_id"));
            discovery.setName(resultSet.getString("name"));
            discovery.setDescription(resultSet.getString("description"));
            discovery.setUrl(resultSet.getString("url"));
            discovery.setUpVote(resultSet.getInt("up_vote"));
            discovery.setDownVote(resultSet.getInt("down_vote"));
            discovery.setTimestamp(resultSet.getTimestamp("date"));
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setUsername(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            discovery.setUser(user);
            return discovery;
        }
    }
}