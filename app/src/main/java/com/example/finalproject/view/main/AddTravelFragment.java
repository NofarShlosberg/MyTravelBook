package com.example.finalproject.view.main;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.dialogs.add_dialogs.CarRentalDialog;
import com.example.finalproject.dialogs.add_dialogs.FlightTicketsDialog;
import com.example.finalproject.dialogs.add_dialogs.InsuranceDialog;
import com.example.finalproject.dialogs.add_dialogs.ResidingDialog;
import com.example.finalproject.models.Travel;
import com.example.finalproject.repositories.DocumentRepository;
import com.example.finalproject.repositories.TravelRepository;
import com.example.finalproject.utils.Callback;
import com.example.finalproject.utils.DatabaseCallback;
import com.example.finalproject.utils.SelectDateDialog;
import com.example.finalproject.utils.UIUtils;

import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AddTravelFragment extends Fragment {

    private Uri ticketsDocument;
    private Uri residingDocument;
    private Uri insuranceDocument;
    private Uri carDocument;

    transient int tasksCompleted = 0;
    Object lock = new Object();
    private Travel travel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_travel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        travel = new Travel();
        Button addTravelFlightButton = view.findViewById(R.id.add_travel_tickets);
        Button addCarRentalButton = view.findViewById(R.id.add_travel_car);
        Button addInsuranceButton = view.findViewById(R.id.add_travel_insurance);
        Button addResidingButton = view.findViewById(R.id.add_travel_residing);
        Button arrivalDateButton = view.findViewById(R.id.add_travel_selectTravelDate);
        Button returnDateButton = view.findViewById(R.id.add_travel_selectTravelReturnDate);
        Button addTravelButton = view.findViewById(R.id.add_travel_submit);


        TextView ticketsTv = view.findViewById(R.id.add_travel_travelFlightDetails);
        TextView carRentalTv = view.findViewById(R.id.add_travel_travelCarDetails);
        TextView insuranceTv = view.findViewById(R.id.add_travel_travelInsuranceDetails);
        TextView residingTv = view.findViewById(R.id.add_travel_travelResidingDetails);

        TextView arrivalDateTv = view.findViewById(R.id.add_travel_travelDateTv);
        TextView returnDateTv = view.findViewById(R.id.add_travel_returnDateTv);



        arrivalDateButton.setOnClickListener(v ->
                UIUtils.openDateDialog(requireContext(), date -> {
                    travel.setTravelDate(date);
                    arrivalDateTv.setText(SelectDateDialog.getDateString(date));
                }));

        returnDateButton.setOnClickListener(v ->
                UIUtils.openDateDialog(requireContext(), date -> {
                    travel.setReturnDate(date);
                    returnDateTv.setText(SelectDateDialog.getDateString(date));
                }));

        addTravelFlightButton.setOnClickListener(v -> {
            FlightTicketsDialog dialog = new FlightTicketsDialog(ticket -> {
                Toast.makeText(requireContext(), "Ticket added successfully", Toast.LENGTH_LONG).show();
                travel.setFlightTickets(ticket);
                ticketsTv.setText(ticket.toString());
            }, ticket -> {
                openFlightTicketsDocumentSelection();
            });
            dialog.show(getChildFragmentManager(), "add flight ticket");
        });

        addCarRentalButton.setOnClickListener(v -> {
            CarRentalDialog dialog = new CarRentalDialog(carRental -> {
                Toast.makeText(requireContext(), "Car rental added successfully", Toast.LENGTH_LONG).show();
                travel.setCarRental(carRental);
                carRentalTv.setText(carRental.toString());
            }, carRental -> {
                openCarRentalDocumentSelection();
            });
            dialog.show(getChildFragmentManager(), "add car rental");
        });

        addInsuranceButton.setOnClickListener(v -> {
            InsuranceDialog dialog = new InsuranceDialog(insurance -> {
                Toast.makeText(requireContext(), "Car insurance added successfully", Toast.LENGTH_LONG).show();
                travel.setInsurance(insurance);
                insuranceTv.setText(insurance.toString());
            }, insurance -> {
                openInsuranceDocumentSelection();
            });
            dialog.show(getChildFragmentManager(), "add insurance");
        });
        addResidingButton.setOnClickListener(v -> {
            ResidingDialog dialog = new ResidingDialog(residing -> {
                Toast.makeText(requireContext(), "Car residing added successfully", Toast.LENGTH_LONG).show();
                travel.setResiding(residing);
                residingTv.setText(residing.toString());
            }, residing -> {
                openResidingDocumentSelection();
            });
            dialog.show(getChildFragmentManager(), "add residing");
        });

        addTravelButton.setOnClickListener(view1 -> {
            TextView add_travel_dest = view.findViewById(R.id.add_travel_dest);
            String destinationName = add_travel_dest.getText().toString();
            travel.setDestination(destinationName);
            ProgressDialog dialog = new ProgressDialog(requireContext());
            dialog.setTitle("Travel Book");
            dialog.setMessage("Adding new travel...");
            dialog.show();
            uploadDocuments(travel -> {
                dialog.dismiss();
                NavHostFragment.findNavController(AddTravelFragment.this)
                        .popBackStack();
            },dialog);
        });

    }


    public void setTaskCompleted() {
        synchronized (lock) {
            this.tasksCompleted = tasksCompleted + 1;
        }
    }

    private void uploadDocuments(Callback<Travel> callback, ProgressDialog dialog) {

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

      executor.scheduleAtFixedRate(() -> {
            if (tasksCompleted >= 4) { // finished uploading all documents
                TravelRepository repository = new TravelRepository();
                repository.insertDocument(travel, new DatabaseCallback<Travel>() {
                    @Override
                    public void onDatabaseException(Exception e) {
                        System.out.println(e.getMessage());
                        dialog.dismiss();
                        callback.consume(null);
                    }

                    @Override
                    public void consume(Travel travelUploaded) {
                        dialog.dismiss();
                        callback.consume(travelUploaded);
                    }
                });
                executor.shutdown();
            }
        },0,1, TimeUnit.SECONDS);
        if (carDocument != null) {
            // residing document
            DocumentRepository.uploadData(carDocument, "cartDocuments",
                    UUID.randomUUID().toString()
                    , new DatabaseCallback<String>() {
                @Override
                public void onDatabaseException(Exception e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    setTaskCompleted();
                }

                @Override
                public void consume(String documentUrl) {
                    travel.getCarRental().setDocumentUrl(documentUrl);
                    tasksCompleted++;
                }
            });
        } else {
            setTaskCompleted();
        }

        if (ticketsDocument != null) {
            // residing document
            DocumentRepository.uploadData(ticketsDocument, "ticketsDocuments",
                    UUID.randomUUID().toString(), new DatabaseCallback<String>() {
                @Override
                public void onDatabaseException(Exception e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    setTaskCompleted();
                }

                @Override
                public void consume(String documentUrl) {
                    travel.getFlightTickets().setDocumentUrl(documentUrl);
                    setTaskCompleted();
                }
            });
        } else {
            setTaskCompleted();
        }

        if (residingDocument != null) {
            // residing document
            DocumentRepository.uploadData(residingDocument, "residingDocuments",
                    UUID.randomUUID().toString(), new DatabaseCallback<String>() {
                @Override
                public void onDatabaseException(Exception e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    setTaskCompleted();
                }

                @Override
                public void consume(String documentUrl) {
                    travel.getResiding().setDocumentUrl(documentUrl);
                    setTaskCompleted();
                }
            });
        } else {
            setTaskCompleted();
        }

        if (insuranceDocument != null) {
            // insurance document
            DocumentRepository.uploadData(insuranceDocument, "insuranceDocuments",
                    UUID.randomUUID().toString(), new DatabaseCallback<String>() {
                @Override
                public void onDatabaseException(Exception e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    setTaskCompleted();
                }

                @Override
                public void consume(String documentUrl) {
                    travel.getInsurance().setDocumentUrl(documentUrl);
                    setTaskCompleted();
                }
            });
        } else {
            setTaskCompleted();
        }
    }


    private void openFlightTicketsDocumentSelection() {
        MainActivity activity = (MainActivity) requireActivity();
        UIUtils.openDocumentPicker(uri -> {
            this.ticketsDocument = uri;
            System.out.println(uri);
        }, activity);
    }

    private void openCarRentalDocumentSelection() {
        MainActivity activity = (MainActivity) requireActivity();
        UIUtils.openDocumentPicker(uri -> {
            this.carDocument = uri;
            System.out.println(uri);
        }, activity);
    }

    private void openInsuranceDocumentSelection() {
        MainActivity activity = (MainActivity) requireActivity();
        UIUtils.openDocumentPicker(uri -> {
            this.insuranceDocument = uri;
            System.out.println(uri);
        }, activity);
    }

    private void openResidingDocumentSelection() {
        MainActivity activity = (MainActivity) requireActivity();
        UIUtils.openDocumentPicker(uri -> {
            this.residingDocument = uri;
            System.out.println(uri);
        }, activity);
    }
}
