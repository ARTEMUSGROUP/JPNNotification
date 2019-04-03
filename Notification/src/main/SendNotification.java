package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import beans.NotificationBean;

public class SendNotification {
	ArrayList<String> scacList = new ArrayList<String>();
	ArrayList<ArrayList<NotificationBean>> notifications = new ArrayList<ArrayList<NotificationBean>>();

	/*public SendNotification(){
		try{
			init();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	 */
	public void init() throws Exception {
		getNotificationDetail();
		for(int i=0; i<scacList.size();i++){
			String billNo="";
			String status = "";
			String statusDesc = "";
			String devices = "";
			for(int j=0; j < notifications.get(i).size(); j++){
				billNo = notifications.get(i).get(j).getBillNo();
				status = notifications.get(i).get(j).getStatusCode();
				statusDesc = notifications.get(i).get(j).getStatusDesc();
				devices = notifications.get(i).get(j).getFireBaseToken();
				if(devices != null){
					try {
						URL url = new URL("https://fcm.googleapis.com/fcm/send");
						// make connection
						URLConnection urlc = url.openConnection();
						// It Content Type is so importan to support JSON call
						urlc.setRequestProperty("Content-Type", "application/json");
						urlc.setRequestProperty("Authorization",
								"key=AIzaSyCijwqOu_fccdb7X2ZEYjV8bCY-vWN0pDA");
						Msj("Url: " + url.toString());
						urlc.setDoOutput(true);
						urlc.setAllowUserInteraction(false);
						String data = "{ \"data\": {"
								+ "\"billNo\": \""+billNo+"\","
								+ "\"status\": \""+status+"\","
								+ "\"statusDesc\": \""+statusDesc+"\""
								+ " },"
								+ "\"to\" : \""+devices+"\""
								+ "}";

						// send query
						PrintStream ps = new PrintStream(urlc.getOutputStream());
						ps.print(data);
						Msj("Sent: " + data);
						ps.close();

						// get result
						BufferedReader br = new BufferedReader(new InputStreamReader(
								urlc.getInputStream()));
						String l = null;
						while ((l = br.readLine()) != null) {
							Msj(l);
						}
						br.close();
					} catch (Exception e) {
						Msj("Error ocurrido");
						Msj(e.toString());
					}
				}
				updateNotifyStatus(notifications.get(i).get(j).getNotifyId());
			}
		}
	}

	private static void Msj(String texto) {
		//System.out.println(texto);
	}

	public void getNotificationDetail() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			Properties properties = loadPropertiesFile();
			Class.forName(properties.getProperty("jdbc.driver"));
			connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
			String query = "select notify_id, notify_scac, bill_no, status, status_code, status_desc, notify_status, entryDate, fire_base_token "
					+ " from notifications n left join user_devices ud on n.notify_scac = ud.login_scac where notify_status = 'SUBMIT'";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if(scacList.contains(resultSet.getString("notify_scac"))){
					int id = scacList.indexOf(resultSet.getString("notify_scac"));
					NotificationBean notificationBean = new NotificationBean();
					notificationBean.setNotifyId(resultSet.getInt("notify_id"));
					notificationBean.setNotifyScac(resultSet.getString("notify_scac"));
					notificationBean.setBillNo(resultSet.getString("bill_no"));
					notificationBean.setStatus(resultSet.getString("status"));
					notificationBean.setStatusCode(resultSet.getString("status_code"));
					notificationBean.setStatusDesc(resultSet.getString("status_desc"));
					notificationBean.setNotifyStatus(resultSet.getString("notify_status"));
					notificationBean.setEntryDate(resultSet.getString("entryDate"));
					notificationBean.setFireBaseToken(resultSet.getString("fire_base_token"));
					notifications.get(id).add(notificationBean);
				} else {
					scacList.add(resultSet.getString("notify_scac"));
					ArrayList<NotificationBean> list = new ArrayList<NotificationBean>();
					NotificationBean notificationBean = new NotificationBean();
					notificationBean.setNotifyId(resultSet.getInt("notify_id"));
					notificationBean.setNotifyScac(resultSet.getString("notify_scac"));
					notificationBean.setBillNo(resultSet.getString("bill_no"));
					notificationBean.setStatus(resultSet.getString("status"));
					notificationBean.setStatusCode(resultSet.getString("status_code"));
					notificationBean.setStatusDesc(resultSet.getString("status_desc"));
					notificationBean.setNotifyStatus(resultSet.getString("notify_status"));
					notificationBean.setEntryDate(resultSet.getString("entryDate"));
					notificationBean.setFireBaseToken(resultSet.getString("fire_base_token"));
					list.add(notificationBean);
					notifications.add(list);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if (statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateNotifyStatus(int notifyId) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			Properties properties = loadPropertiesFile();
			Class.forName(properties.getProperty("jdbc.driver"));
			connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
			String query = "update notifications set notify_status = 'SENT' where notify_id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, notifyId);
			statement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if (statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public Properties loadPropertiesFile() throws Exception {
		Properties properties = new Properties();
		File objmFile = new File("config.properties");
		FileInputStream inputStream = new FileInputStream(objmFile);
		//InputStream inputStream = SendNotification.class.getResourceAsStream("config.properties");
		//System.out.println("inpustream :"+inputStream);
		properties.load(inputStream);
		inputStream.close();

		return properties;
	} 
	
	public static void main(String[] args) {
		SendNotification objSendNotification = new SendNotification();
		try {
			objSendNotification.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
