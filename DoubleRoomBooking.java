public class DoubleRoomBooking extends Exception{
    DoubleRoomBooking(String message){
        super(message);
    }
}
class BookingTimePassed extends  Exception{
    public BookingTimePassed(String message) {
        super(message);
    }
}
class movieDuration extends  Exception{
    public movieDuration(String message) {
        super(message);
    }
}


