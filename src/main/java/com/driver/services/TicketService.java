package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
       //And the end return the ticketId that has come from db

        int trainId=bookTicketEntryDto.getTrainId();
        if(trainRepository.findById(trainId).isPresent()==false){
            throw new Exception("Invalid Train id");
        }
        List<Integer>listOfPassengersIds=bookTicketEntryDto.getPassengerIds();
        int bookingPersonId=bookTicketEntryDto.getBookingPersonId();
        String fromstation=bookTicketEntryDto.getFromStation().toString();
        String tostation=bookTicketEntryDto.getToStation().toString();
        int reqSeats=bookTicketEntryDto.getNoOfSeats();
        Train train=trainRepository.findById(trainId).get();
        List<Ticket> bookedTickets=train.getBookedTickets();
        String route=train.getRoute();
        String[]arr=route.split(",");
        boolean flag1=false,flag2=false;
        for(String str:arr){
            if(str.equalsIgnoreCase(fromstation)){
                flag1=true;
            }
            if(str.equalsIgnoreCase(tostation)){
                flag2=true;
            }
        }
        if(!(flag1 && flag2)){
            throw new Exception("Invalid stations");
        }
        int availbleSeats=train.getNoOfSeats();
        int c=0;

        for(Ticket ticket:bookedTickets){

        }
        if(availbleSeats<reqSeats){
            throw new Exception("Less tickets are available");
        }








        int count=0;int i=0;
        for(i=0;i<arr.length;i++){
            if(arr[i].equalsIgnoreCase(fromstation)){
                break;
            }
        }
        while(!(arr[i].equalsIgnoreCase(tostation))){
            count++;
            i++;
        }
        int cost=count*reqSeats*300;

        Ticket ticket=new Ticket();
        ticket.setFromStation(bookTicketEntryDto.getFromStation());
        ticket.setToStation(bookTicketEntryDto.getToStation());
        ticket.setTrain(train);
        ticket.setTotalFare(cost);
        List<Passenger>passengerList=ticket.getPassengersList();
        for(int j:listOfPassengersIds){
            Passenger passenger=passengerRepository.findById(j).get();
            passengerList.add(passenger);
        }
        Passenger passenger=passengerRepository.findById(bookingPersonId).get();
        passenger.getBookedTickets().add(ticket);
        Ticket saveTicket=ticketRepository.save(ticket);
        return saveTicket.getTicketId();
    }
}
