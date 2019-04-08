package utilities;

import java.nio.ByteBuffer;

public class Employee {
	byte[] id;
	byte[] name;
	byte[] department;
	byte[] salary;
	
	int idSize = 4;
	int nameSize = 30;
	int deptSize = 4;
	int salarySize = 4;
	
	public Employee(byte[] record) { 
		int id = 0;
		int start = 1;
		while(record[start] != ',') { 
			id = id * 10; 
			id += record[start++] - '0';
		}
		this.id = ByteBuffer.allocate(idSize).putInt(id).array();
		start++;
		byte[] name = new byte[nameSize];
		int index = 0;
		while(record[start] != ',') { 
			name[index++] = record[start++];
		}
		this.name = name;
		start++;
		index = 0;
		byte[] deptName = new byte[deptSize];
		while(record[start] != ',') { 
			deptName[index++] = record[start++];
		}
		this.department = deptName;
		start++;
		int salary = 0;
		while(start < record.length - 1) { 
			salary = salary * 10; 
			salary += record[start++] - '0';
		}
		this.salary = ByteBuffer.allocate(salarySize).putInt(salary).array();
	}
	
	public byte[] getByteArray() { 
		byte[] recordBytes = new byte[42];
		for(int i = 0; i < 4; i++) { 
			recordBytes[i] = this.id[i];
		}
		for(int i = 4; i < 34; i++) { 
			recordBytes[i] = this.name[i - 4];
		}
		for(int i = 34; i < 38; i++) { 
			recordBytes[i] = this.department[i - 34];
		}
		for(int i = 38; i < 42; i++) { 
			recordBytes[i] = this.salary[i - 38];
		}
		return recordBytes;
	}
	
	@Override
	public String toString() { 
		String res = "";
		res += "{\"id\":" + ByteBuffer.wrap(this.id).getInt() + ", ";
		res += "Name:" + new String(this.name) + ", ";
		res += "Department:" + new String(this.department) + ", ";
		res += "Salary:" + ByteBuffer.wrap(this.salary).getInt();
		res += "}";
		return res;
	}
}
