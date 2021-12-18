package com.apnagodam.staff.activity.convancy_voachar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreateConveyancePostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass;
import com.apnagodam.staff.databinding.ActivityEmpConveyanceBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UploadConveyanceVoacharClass extends BaseActivity<ActivityEmpConveyanceBinding> {
    String packagingTypeID = null;
    // role of image
    String UserName, CaseID = "";
    public File fileReport, fileCommudity,FileOther;
    boolean ReportsFileSelect = false;
    boolean CommudityFileSelect = false;
    boolean OtherFileSelect = false;
    private String reportFile, commudityFile,OtherFile;
    private Calendar calender;
    // approved for
    List<String> approveName;
    List<String> approveID;
    ArrayAdapter<String> SpinnerApproveByAdapter;
    String SelectedApproveIDIs = null;

    // Terminal Name
    List<String> TerminalName;
    List<String> TerminalID;
    ArrayAdapter<String> SpinnerTerminalAdapter;
    String SelectedTerminalIDIs = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_emp_conveyance;
    }
    @Override
    protected void setUp() {
        calender = Calendar.getInstance();
        // approved for
        approveName = new ArrayList<>();
        approveID = new ArrayList<>();
        approveName.add(getResources().getString(R.string.approved_by));
        approveID.add("0");
        // terminal for
        TerminalName = new ArrayList<>();
        TerminalID = new ArrayList<>();
        TerminalName.add(getResources().getString(R.string.terminal_name));
        TerminalID.add("0");
        binding.tvDone.setVisibility(View.GONE);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // get approve person  list
        apiService.getlevelwiselist("").enqueue(new NetworkCallback<AllLevelEmpListPojo>(getActivity()) {
            @Override
            protected void onSuccess(AllLevelEmpListPojo body) {
                for (int i = 0; i < body.getData().size(); i++) {
                    if (body.getRequest_count() > 0) {
                        binding.tvDone.setClickable(true);
                        binding.tvDone.setEnabled(true);
                        binding.tvDone.setText("Approval Request " + "(" + body.getRequest_count() + ")");
                    }
                    approveID.add(body.getData().get(i).getUserId());
                    approveName.add(body.getData().get(i).getFirstName() + " " + body.getData().get(i).getLastName() + "(" + body.getData().get(i).getEmpId() + ")");
                }
                for (int i = 0; i < body.getWarehouse_name().size(); i++) {
                    TerminalID.add(body.getWarehouse_name().get(i).getId());
                    TerminalName.add(body.getWarehouse_name().get(i).getName() + "(" + body.getWarehouse_name().get(i).getWarehouse_code() + ")");
                }
            }
        });
        clickListner();
        binding.tilStartReading.setVisibility(View.GONE);
        binding.tilEndReading.setVisibility(View.GONE);
        binding.tilKms.setVisibility(View.GONE);
        binding.tilCharges.setVisibility(View.GONE);
        binding.tilOtherExpense.setVisibility(View.GONE);
        binding.etEndReading.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etStartReading.getText().toString().trim() != null && !binding.etStartReading.getText().toString().trim().isEmpty() && !binding.etEndReading.getText().toString().trim().isEmpty()) {
                        Double startReadingLenth = (Double.parseDouble(binding.etStartReading.getText().toString().trim()));
                        Double endReadingLenth = (Double.parseDouble(binding.etEndReading.getText().toString().trim()));
                        if (endReadingLenth > startReadingLenth) {
                            finalAmountCalculation("End");
                        } else {
                            Utility.showAlertDialog(UploadConveyanceVoacharClass.this, getString(R.string.alert), "End Reading Must be Greater than Start reading", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etEndReading.setText("");
                                    binding.etTotal.setText("");
                                }
                            });
                        }
                    }
                    // Toast.makeText(MainActivity.this, "focus loosed", Toast.LENGTH_LONG).show();
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.etStartReading.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (binding.etEndReading.getText().toString().trim() != null && !binding.etEndReading.getText().toString().trim().isEmpty() && !binding.etStartReading.getText().toString().trim().isEmpty()) {
                        Double startReadingLenth = (Double.parseDouble(binding.etStartReading.getText().toString().trim()));
                        Double endReadingLenth = (Double.parseDouble(binding.etEndReading.getText().toString().trim()));
                        if (endReadingLenth > startReadingLenth) {
                            finalAmountCalculation("End");
                        } else {
                            Utility.showAlertDialog(UploadConveyanceVoacharClass.this, getString(R.string.alert), "Start Reading Must be less than End reading", new Utility.AlertCallback() {
                                @Override
                                public void callback() {
                                    binding.etStartReading.setText("");
                                    binding.etTotal.setText("");
                                }
                            });
                        }
                    }
                } else {
                    /// Toast.makeText(MainActivity.this, "focused", Toast.LENGTH_LONG).show();
                }
            }
        });
        // spinner Conveyance type
        binding.etEndReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               /* if (charSequence.length() != 0 && !charSequence.equals("")) {
                    finalAmountCalculation("End");
                } else {
                    Log.d("printchecxk", "For Testing Now");
                }*/

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0 && !editable.equals("")) {
                    finalAmountCalculation("End");
                } else {
                    binding.etKms.setText("");
                    Log.d("printchecxk", "For Testing NowEnd");
                }
            }
        });
        binding.etStartReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.equals("")) {
                    finalAmountCalculation("Start");
                } else {
                    binding.etKms.setText("");
                    Log.d("printchecxk", "For Testing Now..");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etOtherExpense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.equals("")) {
                    finalAmountExpensionCalculation();
                } else {
                    binding.etTotal.setText("0");
                    finalAmountCalculation("");
                    Log.d("printchecxk", "For Testing Now");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.spinnerConvType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    packagingTypeID = parent.getItemAtPosition(position).toString();
                    binding.tilStartReading.setVisibility(View.VISIBLE);
                    binding.tilEndReading.setVisibility(View.VISIBLE);
                    binding.tilKms.setVisibility(View.VISIBLE);
                    binding.tilCharges.setVisibility(View.VISIBLE);
                    binding.tilOtherExpense.setVisibility(View.VISIBLE);
                    UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
                    if (packagingTypeID.equalsIgnoreCase("Two-wheeler")) {
                        if (userDetails.getTwo_wheeler_rate() != null) {
                            binding.etCharges.setText(userDetails.getTwo_wheeler_rate());
                        } else {
                            binding.etCharges.setText("0");
                        }
                        finalAmountCalculation("");
                    } else {
                        if (userDetails.getTwo_wheeler_rate() != null) {
                            binding.etCharges.setText(userDetails.getFour_wheeler_rate());
                        } else {
                            binding.etCharges.setText("0");
                        }
                        finalAmountCalculation("");
                    }
                    if (position == 3) {
                        binding.tilStartReading.setVisibility(View.GONE);
                        binding.tilEndReading.setVisibility(View.GONE);
                        binding.tilKms.setVisibility(View.GONE);
                        binding.tilCharges.setVisibility(View.GONE);
                        binding.tilOtherExpense.setVisibility(View.VISIBLE);
                        binding.etStartReading.setText("");
                        binding.etEndReading.setText("");
                        binding.etKms.setText("");
                        binding.etCharges.setText("");
                        binding.etTotal.setText("");
                        if (!binding.etOtherExpense.getText().toString().trim().isEmpty() && binding.etOtherExpense.getText().toString().trim() != null && Double.parseDouble(binding.etOtherExpense.getText().toString().trim()) > 0) {
                            finalAmountExpensionCalculation();
                        } else {
                            binding.etTotal.setText("0");
                            finalAmountCalculation("");
                        }

                    }
                } else {
                    binding.tilStartReading.setVisibility(View.GONE);
                    binding.tilEndReading.setVisibility(View.GONE);
                    binding.tilKms.setVisibility(View.GONE);
                    binding.tilCharges.setVisibility(View.GONE);
                    binding.tilOtherExpense.setVisibility(View.GONE);
                    binding.etStartReading.setText("");
                    binding.etEndReading.setText("");
                    binding.etKms.setText("");
                    binding.etCharges.setText("");
                    binding.etTotal.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });

        // Approved listing
        SpinnerApproveByAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, approveName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerApproveByAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerApprovedBy.setAdapter(SpinnerApproveByAdapter);
        binding.spinnerApprovedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String EmpID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < approveName.size(); i++) {
                        if (EmpID.equalsIgnoreCase(approveName.get(position))) {
                            SelectedApproveIDIs = approveID.get(position);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // Terminal listing
        SpinnerTerminalAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, TerminalName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerTerminalAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerTerminal.setAdapter(SpinnerTerminalAdapter);
        binding.spinnerTerminal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String EmpID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < TerminalName.size(); i++) {
                        if (EmpID.equalsIgnoreCase(TerminalName.get(position))) {
                            SelectedTerminalIDIs = TerminalID.get(position);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }
    public void finalAmountCalculation(String action) {
        try {
            if (!binding.etStartReading.getText().toString().equals("") && !binding.etEndReading.getText().toString().equals("") &&
                    Double.parseDouble(binding.etStartReading.getText().toString()) > 0 && Double.parseDouble(binding.etEndReading.getText().toString()) > 0) {
                double startNormal = Double.parseDouble((binding.etStartReading.getText().toString()));
                double endActual = Double.parseDouble(binding.etEndReading.getText().toString());
                if (endActual > startNormal) {
                    double actualReading = endActual - startNormal;
                    binding.etKms.setText("" + actualReading);
                    double totalamount = 0.0;
                    if (binding.etOtherExpense.getText().toString() != null && !(binding.etOtherExpense.getText().toString()).isEmpty()) {
                        totalamount = (Double.parseDouble((binding.etKms.getText().toString())) * Double.parseDouble((binding.etCharges.getText().toString()))) + Double.parseDouble((binding.etOtherExpense.getText().toString()));
                    } else {
                        totalamount = (Double.parseDouble((binding.etKms.getText().toString())) * Double.parseDouble((binding.etCharges.getText().toString())));
                    }
                    double roundedNumber = Utility.round(totalamount, 2);
                    binding.etTotal.setText("" + roundedNumber);
                } else {
                    binding.etKms.setText("");

                    /*if (action.equalsIgnoreCase("End")) {
                        binding.etEndReading.setText("0");
                        Toast.makeText(UploadConveyanceVoacharClass.this, "End Reading Must be Greater than Start reading", Toast.LENGTH_SHORT).show();
                    } else if (action.equalsIgnoreCase("Start")) {
                        binding.etStartReading.setText("0");
                        Toast.makeText(UploadConveyanceVoacharClass.this, "Start Reading Must be less than End reading", Toast.LENGTH_SHORT).show();
                    }*/
                }
            } else {
                // binding.etEndReading.setText("0.0");
            }

        } catch (Exception e) {
            Log.e("getNotification", e + "");
        }
    }

    public void finalAmountExpensionCalculation() {
        try {
            if (!binding.etOtherExpense.getText().toString().equals("") && !binding.etOtherExpense.getText().toString().isEmpty()) {
                double expansionAmount = Double.parseDouble((binding.etOtherExpense.getText().toString()));
                if (binding.etTotal.getText().toString() != null && !(binding.etTotal.getText().toString()).isEmpty()) {
                    double totalAmount = Double.parseDouble(binding.etTotal.getText().toString());
                    double actualReading = expansionAmount;
                    double roundedNumber = Utility.round(actualReading, 2);
                    binding.etTotal.setText("" + roundedNumber);
                } else {
                    double roundedNumber = Utility.round(expansionAmount, 2);
                    binding.etTotal.setText("" + roundedNumber);
                }
            } else {
                binding.etOtherExpense.setText("");
                // binding.etEndReading.setText("0.0");
            }
            finalAmountCalculation("");
        } catch (Exception e) {
            Log.e("getNotification", e + "");
        }
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(MyConveyanceListClass.class);
            }
        });
        binding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(ApprovalRequestConveyanceListClass.class);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(UploadConveyanceVoacharClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (isValid()) {
                            if (TextUtils.isEmpty(stringFromView(binding.userCommitmentDate))) {
                                Toast.makeText(UploadConveyanceVoacharClass.this, "Select Date", Toast.LENGTH_LONG).show();
                            } else if (packagingTypeID == null) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.slect_conv_type), Toast.LENGTH_LONG).show();
                            } else if (fileReport == null) {
                                Toast.makeText(getApplicationContext(), R.string.start_meter_image, Toast.LENGTH_LONG).show();
                            } else if (fileCommudity == null) {
                                Toast.makeText(getApplicationContext(), R.string.end_meter_image, Toast.LENGTH_LONG).show();
                            } else if (SelectedApproveIDIs == null) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.approved_by), Toast.LENGTH_LONG).show();
                            } else if (SelectedTerminalIDIs == null) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.terminal_name), Toast.LENGTH_LONG).show();
                            } else if (packagingTypeID.equalsIgnoreCase("Two-wheeler") || packagingTypeID.equalsIgnoreCase("Four-wheeler")) {
                                if (TextUtils.isEmpty(stringFromView(binding.etStartReading))) {
                                    Toast.makeText(UploadConveyanceVoacharClass.this, "Enter Start meter Reading ", Toast.LENGTH_LONG).show();
                                }else  if (TextUtils.isEmpty(stringFromView(binding.etEndReading))) {
                                    Toast.makeText(UploadConveyanceVoacharClass.this, "Enter End meter Reading", Toast.LENGTH_LONG).show();
                                }else {
                                    onNext();
                                }
                            } else {
                                onNext();
                            }
                        }
                    }
                });
            }
        });
        binding.uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportsFileSelect = true;
                CommudityFileSelect = false;
                OtherFileSelect =false;
                callImageSelector();
            }
        });
        binding.uploadOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportsFileSelect = false;
                CommudityFileSelect = false;
                OtherFileSelect =true;
                callImageSelector();
            }
        });
        binding.uploadCommudity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportsFileSelect = false;
                CommudityFileSelect = true;
                OtherFileSelect =false;
                callImageSelector();
            }
        });
        binding.OtherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadConveyanceVoacharClass.this, R.layout.popup_photo_full, view, OtherFile, null);
            }
        });
        binding.ReportsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadConveyanceVoacharClass.this, R.layout.popup_photo_full, view, reportFile, null);
            }
        });
        binding.CommudityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoFullPopupWindow(UploadConveyanceVoacharClass.this, R.layout.popup_photo_full, view, commudityFile, null);
            }
        });
        binding.lpCommiteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDatePicker();
            }
        });
        binding.userCommitmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDatePicker();
            }
        });
    }

    public void popUpDatePicker() {
        calender = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(UploadConveyanceVoacharClass.this, date, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        calender.add(Calendar.DATE, -1); // subtract 1 day from Today
        dateDialog.getDatePicker().setMinDate(calender.getTimeInMillis());
        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calender.set(Calendar.YEAR, year);
            calender.set(Calendar.MONTH, monthOfYear);
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.userCommitmentDate.setText(sdf.format(calender.getTime()).toString());
        }
    };

    private void callImageSelector() {
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restrict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientation
                .setPath("/apnagodam/lp/images");                                       //Custom Path For media Storage
        Pix.start(UploadConveyanceVoacharClass.this, options.setRequestCode(REQUEST_CAMERA_PICTURE));
    }

    // update file
    public void onNext() {
        String KanthaImage = "", CommudityFileSelectImage = "",otherFileImage="";
        if (fileReport != null) {
            KanthaImage = "" + Utility.transferImageToBase64(fileReport);
        }
        if (fileCommudity != null) {
            CommudityFileSelectImage = "" + Utility.transferImageToBase64(fileCommudity);
        }
        if (FileOther != null) {
            otherFileImage = "" + Utility.transferImageToBase64(FileOther);
        }
        apiService.doCreateConveyance(new CreateConveyancePostData(
                stringFromView(binding.userCommitmentDate), KanthaImage, CommudityFileSelectImage,otherFileImage,
                stringFromView(binding.etVehicleNo), stringFromView(binding.etFrom), stringFromView(binding.etTo), stringFromView(binding.etStartReading),
                stringFromView(binding.etEndReading), stringFromView(binding.etKms), stringFromView(binding.etCharges), stringFromView(binding.etLocation),
                stringFromView(binding.etOtherExpense), stringFromView(binding.etTotal), stringFromView(binding.notes), SelectedApproveIDIs, packagingTypeID, SelectedTerminalIDIs)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(UploadConveyanceVoacharClass.this, getString(R.string.alert),  body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        startActivityAndClear(MyConveyanceListClass.class);
                    }
                });
            }
        });
    }

    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etTotal))) {
            return Utility.showEditTextError(binding.tilTotal, R.string.enter_total);
        } else if (TextUtils.isEmpty(stringFromView(binding.notes))) {
            return Utility.showEditTextError(binding.notes, R.string.purpose);
        } if (TextUtils.isEmpty(stringFromView(binding.etVehicleNo))) {
            return Utility.showEditTextError(binding.tilVehicleNo, R.string.vehicle_no_conv);
        } else if (TextUtils.isEmpty(stringFromView(binding.etFrom))) {
            return Utility.showEditTextError(binding.tilFrom, R.string.from_place_con_list);
        }else if (TextUtils.isEmpty(stringFromView(binding.etTo))) {
            return Utility.showEditTextError(binding.tilTo, R.string.to_place_con_list);
        }else if (TextUtils.isEmpty(stringFromView(binding.etLocation))) {
            return Utility.showEditTextError(binding.tilLocation, R.string.enter_location);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert returnValue != null;
            Log.e("getImageesValue", returnValue.get(0).toString());
            if (requestCode == REQUEST_CAMERA_PICTURE) {
                if (ReportsFileSelect) {
                    ReportsFileSelect = false;
                    CommudityFileSelect = false;
                    OtherFileSelect = false;
                    fileReport = new File(compressImage(returnValue.get(0).toString()));
                    Uri uri = Uri.fromFile(fileReport);
                    reportFile = String.valueOf(uri);
                    binding.ReportsImage.setImageURI(uri);
                } else if (CommudityFileSelect) {
                    ReportsFileSelect = false;
                    CommudityFileSelect = false;
                    OtherFileSelect = false;
                    fileCommudity = new File(compressImage(returnValue.get(0).toString()));
                    Uri uri = Uri.fromFile(fileCommudity);
                    commudityFile = String.valueOf(uri);
                    binding.CommudityImage.setImageURI(uri);
                }
                else if (OtherFileSelect) {
                    ReportsFileSelect = false;
                    CommudityFileSelect = false;
                    OtherFileSelect = false;
                    FileOther = new File(compressImage(returnValue.get(0).toString()));
                    Uri uri = Uri.fromFile(FileOther);
                    OtherFile = String.valueOf(uri);
                    binding.OtherImage.setImageURI(uri);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Options.init().setRequestCode(100));
                } else {
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
