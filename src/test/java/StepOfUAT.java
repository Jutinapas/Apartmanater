import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import model.Room;
import model.DBConnector;
import model.TypeRoom;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StepOfUAT {

    DBConnector ins;
    @Before
    public void setup() {
        ins = DBConnector.getDBConnector();
    }

    // 1 Edit Apartment
    @Given("^หอพักมีชี่อหอพักเป็น (.*) และวันชำระเงินประจำเดือนเป็นวันที่ (\\d+)")
    public void given_apartment(String name, int day) {
        ins.updateApartmentNameAndDatePayMoney(name,day + "");
    }
    @When("^กดแก้ไขข้อมูลหอพัก จากนั้นใส่ชื่อหอพักเป็น (.*) และวันชำระเงินประจำเดือนเป็นวันที่ (\\d+) แล้วกดตกลง")
    public void when_edit_apartment(String name, int day) {
        ins.updateApartmentNameAndDatePayMoney(name,day + "");
    }
    @Then("^ชื่อหอพักคือ (.*) และวันชำระเงินประจำเดือนคือวันที่ (\\d+)")
    public void then_edit_apartment(String name, int day) {
        assertEquals(name, ins.selectNameOfApartment());
        assertEquals(day + "", ins.selectDatePayMoney());
    }

    // 2 Create Type Room
    int type_room_size_2;
    @When("^กดเพิ่มประเภท จากนั้นใส่ชื่อประเภทเป็น (.*) และราคาค่าเช่ารายเดือนเป็น (\\d+) ราคาค่าเช่ารายวันเป็น (\\d+) แล้วกดตกลง")
    public void when_create_type_room(String name, double monthly, double daily) {
        type_room_size_2 = ins.selectAllTypeRoom().size();
        ins.insertTypeRoom(name, monthly, daily);
    }
    @Then("^มีประเภทห้องใหม่ชื่อประเภทเป็น (.*) และราคาค่าเช่ารายเดือนเป็น (\\d+) ราคาค่าเช่ารายวันเป็น (\\d+) และจำนวนประเภทห้องเพิ่มขึ้น")
    public void then_create_type_room(String name, double monthly, double daily) {
        TypeRoom new_type = ins.getTypeRoomByID(ins.getIDTyperoomFromNameTypeRoom(name));
        assertEquals(name, new_type.getTypeRoom());
        assertEquals(monthly, new_type.getRentPerMonth());
        assertEquals(daily, new_type.getRentPerDay());
        assertEquals(type_room_size_2, ins.selectAllTypeRoom().size());
    }

    // 3 Edit Type Room
    @Given("^มีประเภทห้องที่ชื่อประเภทเป็น (.*) และราคาค่าเช่ารายเดือนเป็น (\\d+) ราคาค่าเช่ารายวันเป็น (\\d+)")
    public void given_edit_type_room(String name, double monthly, double daily) {
        ins.insertTypeRoom(name, monthly, daily);
    }
    @When("^กดแก้ไขประเภทห้องจากประเภทห้อง(.*) จากนั้นใส่ชื่อประเภทเป็น (.*) และราคาค่าเช่ารายเดือนเป็น (\\d+) ราคาค่าเช่ารายวันเป็น (\\d+) แล้วกดตกลง")
    public void when_edit_type_room(String name1, String name, double monthly, double daily) {
        int id = ins.getRecentTypeRoom();
        ins.updateTypeRoom(id, name, monthly, daily);
    }
    @Then("^ประเภทห้องเดิมถูกมีชื่อประเภทคือ (.*) และมีราคาค่าเช่ารายเดือนคือ (\\d+) ราคาค่าเช่ารายวันคือ (\\d+)")
    public void then_edit_type_room(String name, double monthly, double daily) {
        TypeRoom new_type = ins.getTypeRoomByID(ins.getIDTyperoomFromNameTypeRoom(name));
        assertEquals(new_type.getTypeRoom(), name);
        assertEquals(new_type.getRentPerMonth(), monthly);
        assertEquals(new_type.getRentPerDay(), daily);
    }

    // 4 Delete Type Room
    int type_room_size_4;
    @When("^กดลบประเภทห้องจากประเภทห้อง(.*)")
    public void when_delete_type_room(String name) {
        type_room_size_4 = ins.selectAllTypeRoom().size();
        ins.deleteTypeRoom(ins.getRecentTypeRoom());
    }
    @Then("^ประเภทห้องถูกลบ และจำนวนประเภทห้องลดลง")
    public void then_delete_type_room() {
        assertEquals(type_room_size_4 - 1, ins.selectAllTypeRoom().size());
    }

    // 5 Create Room
    int room_size_5;
    @When("^กดเพิ่มห้อง จากนั้นใส่ชื่อห้องเป็น (.*) ชั้น (\\d+) และประเภทเป็น(.*) แล้วกดตกลง")
    public void when_create_room(String name, int floor, String type) {
        room_size_5 = ins.selectAllRoom().size();
        ins.insertRoom(name, ins.getIDTyperoomFromNameTypeRoom(type), floor);
    }
    @Then("^มีห้องใหม่ และจำนวนห้องเพิ่มขึ้น")
    public void then_create_room() {
        assertEquals(room_size_5 + 1, ins.selectAllRoom().size());
    }

    // 6 Edit Room
    @Given("^มีห้องชื่อ (.*) ชั้น (\\d+) และประเภทเป็น(.*)")
    public void given_edit_room(String name, int floor, String type) {
        ins.insertRoom(name, ins.getIDTyperoomFromNameTypeRoom(type), floor);
    }
    @When("^กดแก้ไขห้องจาก ห้อง (.*) จากนั้นใส่ชื่อห้องเป็น (.*) ชั้น (\\d+) และประเภทเป็น(.*) แล้วกดตกลง")
    public void when_edit_room(String name1, String name2, int floor, String type) {
        ins.updateRoom(ins.getIDroomByNameRoom(name1), name2, floor, ins.getIDTyperoomFromNameTypeRoom(type));
    }
    @Then("^ห้องเดิมถูกแก้ไขโดยมีชื่อห้องเป็น (.*) ชั้น (\\d+) และประเภทเป็น(.*)")
    public void then_edit_room(String name, int floor, String type) {
        Room new_room = ins.getRoomByID(ins.getIDroomByNameRoom(name));
        assertEquals(name, new_room.getRoom_name());
        assertEquals(floor, new_room.getFloor());
        assertEquals(ins.getIDTyperoomFromNameTypeRoom(type), new_room.getId_type_room());
    }

    // 7 Delete Room
    int room_size_7;
    @When("^กดลบห้องจากห้อง (.*)")
    public void when_delete_room(String name) {
        room_size_7 = ins.selectAllRoom().size();
        ins.deleteRoom(ins.getIDroomByNameRoom(name));
    }
    @Then("^ห้องถูกลบ และจำนวนห้องลดลง")
    public void then_delete_room() {
        assertEquals(room_size_7 - 1, ins.selectAllRoom().size());
    }

    // 8 Searching
    int id1;
    int set_size;
    int room_size_8;
    @Given("^มีห้อง (.*) โดยมีการจองของห้อง 101 แบบ(.*) ตั้งแต่วันที่ (.*) เป็นเวลา (\\d+) เดือน ลูกค้าคือ (.*) เบอร์โทรคือ (.*)")
    public void given_searching(String name1, String type, String in, int month, String name, String tel) {
        ins.insertRoom(name1, ins.getRecentTypeRoom(), 1);
        id1 = ins.getRecentRoom();
        room_size_8 = ins.selectAllRoom().size();
        ins.insertReservation(LocalDate.parse(in), LocalDate.parse(in).plusMonths(month), id1, type, name, tel);
    }
    @When("^กดค้นหาการจองตั้งแต่วันที่ (.*) แบบรายเดือนเป็นเวลา (\\d+) เดือน")
    public void when_searching(String in, int month) {
         set_size = ins.selectIDRoomThatReservationNotInRange(LocalDate.parse(in), LocalDate.parse(in).plusMonths(1)).size();
    }
    @Then("^ห้องที่ว่างลดลง")
    public void then_serching() {
        assertEquals(room_size_8 - 1, room_size_8 - set_size);
    }

    // 9 - 10 Reserve
    int reserve_size_9_10;
    @Given("^มีห้อง (.*)")
    public void given_reserve(String name) {
        ins.insertRoom(name, ins.getRecentTypeRoom(), 2);
    }
    @When("^กดเพิ่มการจองจากห้อง (.*) แบบ(.*)เป็นเวลา (\\d+) เดือน ตั้งแต่วันที่ (.*) โดยลูกค้าคือ (.*) และเบอร์โทรศัพท์เป็น (.*)")
    public void when_reserve_monthly(String room, String type, int month, String in, String name, String tel) {
        reserve_size_9_10 = ins.selectReservationWithRoom(ins.getRecentRoom()).size();
        ins.insertReservation(LocalDate.parse(in), LocalDate.parse(in).plusMonths(month), ins.getIDroomByNameRoom(room), type, name, tel);
    }
    @When("^กดเพิ่มการจองจากห้อง (.*) แบบ(.*) ตั้งแต่วันที่ (.*) ถึง (.*) โดยลูกค้าคือ (.*) และเบอร์โทรศัพท์เป็น (.*)")
    public void when_reserve_daily(String room, String type, String in, String out, String name, String tel) {
        reserve_size_9_10 = ins.selectReservationWithRoom(ins.getIDroomByNameRoom(room)).size();
        ins.insertReservation(LocalDate.parse(in), LocalDate.parse(out), ins.getIDroomByNameRoom(room), type, name, tel);
    }
    @Then("^มีการจองใหม่ และจำนวนการจองของห้อง (.*) เพิ่มขึ้น")
    public void then_reserve(String room) {
        assertEquals(reserve_size_9_10 + 1, ins.selectReservationWithRoom(ins.getIDroomByNameRoom(room)).size());
    }

    // 11 Delete Reserve
    int reserve_size_11;
    @Given("^มีการจองของห้อง (.*) แบบ(.*) ตั้งแต่วันที่ (.*) ถึง (.*) โดยลูกค้าคือ (.*) และเบอร์โทรศัพท์เป็น (.*)")
    public void given_delete_reserve(String room, String type, String in, String out, String name, String tel) {
        ins.insertRoom(room, ins.getRecentTypeRoom(), 3);
        ins.insertReservation(LocalDate.parse(in), LocalDate.parse(out), ins.getIDroomByNameRoom(room), type, name, tel);
    }
    @When("^กดลบการจองจากห้อง (.*)")
    public void when_delete_reserve(String name) {
        reserve_size_11 = ins.selectReservationWithRoom(ins.getIDroomByNameRoom(name)).size();
        ins.updateReservationById(ins.getRecentReservation());
    }
    @Then("^การจองถูกลบ และจำนวนการจองของห้อง (.*) ลดลง")
    public void then_delete_reserve(String room) {
        assertEquals(reserve_size_11 - 1, ins.selectReservationWithRoom(ins.getIDroomByNameRoom(room)).size());
    }

}
