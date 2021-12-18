package com.apnagodam.staff.activity.dispaldgeinventory;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.CreateDispladgePostData;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseListingActivity;
import com.apnagodam.staff.activity.in.first_kantaparchi.UploadFirstkantaParchiClass;
import com.apnagodam.staff.databinding.DispladgeShortingBagsBinding;
import com.apnagodam.staff.module.AllLevelEmpListPojo;
import com.apnagodam.staff.module.AllUserListPojo;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.DispladgeCommodityPojo;
import com.apnagodam.staff.utils.PhotoFullPopupWindow;
import com.apnagodam.staff.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadDispladgeClass extends BaseActivity<DispladgeShortingBagsBinding> implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ArrayAdapter<String> SpinnerStackAdapter, SpinnerCommudityAdapter, spinnerTeerminalAdpter, SpinnerUserListAdapter, spinnerEmployeeAdpter;
    String stackID = null, commudityID = null, TerminalID = null,
            seleectCoustomer = null, selectConvertOther = null;
    String UserUniqueID;
    String UserID = null;
    List<String> StackName;
    List<String> StackID;
    List<String> CommudityName;
    List<String> TerminalName;
    List<String> TerminalsID;
    List<String> CustomerName;
    List<String> CustomerID;
    List<String> LeadGenerateOtherName;
    List<String> LeadGenerateOtheID;
    List<AllLevelEmpListPojo.WherHouseName> data;
    List<AllUserListPojo.User> Userdata;
    List<DispladgeCommodityPojo.Datum> Stackdata;
    private List<DispladgeCommodityPojo.Datum> outCommodityListData;
    private List<AllLevelEmpListPojo.Datum> Approvedata;

    public File fileKantha;
    boolean firstKanthaFile = false;
    private String firstkantaParchiFile;
    @Override
    protected int getLayoutResId() {
        return R.layout.displadge_shorting_bags;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        clickListner();
        StackName = new ArrayList<>();
        StackID = new ArrayList<>();
        CommudityName = new ArrayList<>();
        TerminalName = new ArrayList<>();
        TerminalsID = new ArrayList<>();
        CustomerName = new ArrayList<>();
        CustomerID = new ArrayList<>();
        LeadGenerateOtherName = new ArrayList<>();
        LeadGenerateOtheID = new ArrayList<>();

        CommudityName.add(getResources().getString(R.string.commodity_name));
        TerminalName.add(getResources().getString(R.string.terminal_name1));
        CustomerName.add(getResources().getString(R.string.select_coustomer));
        LeadGenerateOtherName.add("If Lead Converted By Other");
        setValueOnSpinner();
        try {
            getUserList();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    private void getoutCommodity() {
        showDialog();
        apiService.getOutDisppladgeCommodityList(TerminalID, UserUniqueID, "", "commodity_names").enqueue(new NetworkCallback<DispladgeCommodityPojo>(getActivity()) {
            @Override
            protected void onSuccess(DispladgeCommodityPojo body) {
                CommudityName.clear();
                if (outCommodityListData != null) {
                    outCommodityListData.clear();
                }
                CommudityName.add(getResources().getString(R.string.commodity));
                outCommodityListData = body.getData();
                for (int i = 0; i < outCommodityListData.size(); i++) {
                    String commodityType = outCommodityListData.get(i).getCommodityType();
                    if (commodityType.equalsIgnoreCase("Primary")) {
                        commodityType = "किसानी";
                    } else {
                        commodityType = "MTP";
                    }
                    CommudityName.add(outCommodityListData.get(i).getCategory() + "(" + commodityType + ")");
                    hideDialog();
                }
                SpinnerCommudityAdapter = new ArrayAdapter<String>(UploadDispladgeClass.this, R.layout.multiline_spinner_item, CommudityName) {
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
                SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                // Set Adapter in the spinner
                binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
                if (commudityID != null) {
                    getstack();
                }
            }
        });
    }

    private void setValueOnSpinner() {
        SpinnerUserListAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, CustomerName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerUserListAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        binding.spinnerUserName.setAdapter(SpinnerUserListAdapter);

        binding.spinnerUserName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // selected item in the list
                if (position != 0) {
                    try {
                        UserID = parentView.getItemAtPosition(position).toString();
                        String[] part = UserID.split("(?<=\\D)(?=\\d)");
                        seleectCoustomer = (part[1]);
                        for (int i = 0; i < Userdata.size(); i++) {
                            if (UserID.equalsIgnoreCase(Userdata.get(i).getFname() + "(" + Userdata.get(i).getPhone())) {
                                UserUniqueID = Userdata.get(i).getUserId();
                            }
                        }
                        if (seleectCoustomer == null || TerminalID == null) {
                        } else {
                            getoutCommodity();
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    UserUniqueID = null;
                    seleectCoustomer = null;
                }
                // SpinnerUserListAdapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
            }
        });

        spinnerEmployeeAdpter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, LeadGenerateOtherName) {
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
        spinnerEmployeeAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerLeadConvertOther.setAdapter(spinnerEmployeeAdpter);
        binding.spinnerLeadConvertOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String EmpID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < Approvedata.size(); i++) {
                        if (EmpID.equalsIgnoreCase(Approvedata.get(i).getFirstName() + " " + Approvedata.get(i).getLastName() + "(" + Approvedata.get(i).getEmpId() + ")")) {
                            selectConvertOther = String.valueOf(Approvedata.get(i).getUserId());
                        }
                    }
                } else {
                    selectConvertOther = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // commodity listing

        SpinnerCommudityAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, CommudityName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
        binding.spinnerCommudity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < outCommodityListData.size(); i++) {
                        String commodityType = outCommodityListData.get(i).getCommodityType();
                        if (commodityType.equalsIgnoreCase("Primary")) {
                            commodityType = "किसानी";
                        } else {
                            commodityType = "MTP";
                        }
                        if (presentMeterStatusID.equalsIgnoreCase(outCommodityListData.get(i).getCategory() + "(" + commodityType + ")")) {
                            commudityID = String.valueOf(outCommodityListData.get(i).getCommodity());
                            getstack();
                            break;
                        }
                    }
                } else {
                    commudityID = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // layout Terminal Listing resource and list of items.
        spinnerTeerminalAdpter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, TerminalName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        spinnerTeerminalAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        binding.spinnerTerminal.setAdapter(spinnerTeerminalAdpter);
        binding.spinnerTerminal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    String presentMeterStatusID = parentView.getItemAtPosition(position).toString();
                    for (int i = 0; i < data.size(); i++) {
                        if (presentMeterStatusID.contains(data.get(i).getName())) {
                            TerminalID = String.valueOf(data.get(i).getId());
                            binding.rlUser.setVisibility(View.VISIBLE);
                            if (seleectCoustomer == null || TerminalID == null) {
                            } else {
                                getoutCommodity();

                            }
                            break;
                        }
                    }
                } else {
                    TerminalID = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // layout Terminal Listing resource and list of items.
        SpinnerStackAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, StackName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerStackAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        binding.spinnerStack.setAdapter(SpinnerStackAdapter);
        binding.spinnerStack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    try {
                       // stackID = String.valueOf(Stackdata.get(position - 1).getId());
                        stackID = parentView.getItemAtPosition(position).toString();
                    } catch (IndexOutOfBoundsException e) {

                    }
                } else {
                    stackID = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }

    private void getstack() {
        apiService.getOutDisppladgeCommodityList(TerminalID, UserUniqueID, commudityID, "stacks").enqueue(new NetworkCallback<DispladgeCommodityPojo>(getActivity()) {
            @Override
            protected void onSuccess(DispladgeCommodityPojo body) {
                StackName.clear();
                stackID = null;
                binding.spinnerStack.setPrompt("");
                StackName.add("Stack Number");
                Stackdata = body.getData();
                for (int i = 0; i < Stackdata.size(); i++) {
                    StackName.add(Stackdata.get(i).getStackNo());
                }
                SpinnerStackAdapter = new ArrayAdapter<String>(UploadDispladgeClass.this, R.layout.multiline_spinner_item, StackName) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        v.setBackgroundColor(Color.parseColor("#05000000"));
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }
                };
                SpinnerStackAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                binding.spinnerStack.setAdapter(SpinnerStackAdapter);
            }
        });
    }


    private void getUserList() {
        apiService.getUserList().enqueue(new NetworkCallback<AllUserListPojo>(getActivity()) {
            @Override
            protected void onSuccess(AllUserListPojo body) {
                Userdata = body.getUsers();
                for (int i = 0; i < Userdata.size(); i++) {
                    CustomerName.add(Userdata.get(i).getFname() + "(" + Userdata.get(i).getPhone());
                }
                SpinnerUserListAdapter = new ArrayAdapter<String>(UploadDispladgeClass.this, R.layout.multiline_spinner_item, CustomerName) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        v.setBackgroundColor(Color.parseColor("#05000000"));
                        ((TextView) v).setTextColor(Color.parseColor("#000000"));
                        return v;
                    }
                };
                SpinnerUserListAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                binding.spinnerUserName.setAdapter(SpinnerUserListAdapter);
                apiService.getlevelwiselist("").enqueue(new NetworkCallback<AllLevelEmpListPojo>(getActivity()) {
                    @Override
                    protected void onSuccess(AllLevelEmpListPojo body) {
                        Approvedata = body.getData();
                        for (int i = 0; i < body.getData().size(); i++) {
                            if (body.getRequest_count() > 0) {
                                binding.tvDone.setClickable(true);
                                binding.tvDone.setEnabled(true);
                                binding.tvDone.setText("Approval Request " + "(" + body.getRequest_count() + ")");
                            }
                            LeadGenerateOtherName.add(body.getData().get(i).getFirstName() + " " + body.getData().get(i).getLastName() + "(" + body.getData().get(i).getEmpId() + ")");
                        }
                        spinnerEmployeeAdpter = new ArrayAdapter<String>(UploadDispladgeClass.this, R.layout.multiline_spinner_item, LeadGenerateOtherName) {
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
                        spinnerEmployeeAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                        // Set Adapter in the spinner
                        binding.spinnerLeadConvertOther.setAdapter(spinnerEmployeeAdpter);
                        data = body.getWarehouse_name();
                        for (int i = 0; i < body.getWarehouse_name().size(); i++) {
                            TerminalName.add(body.getWarehouse_name().get(i).getName() + "(" + body.getWarehouse_name().get(i).getWarehouse_code() + ")");
                        }
                        spinnerTeerminalAdpter = new ArrayAdapter<String>(UploadDispladgeClass.this, R.layout.multiline_spinner_item, TerminalName) {
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View v = super.getView(position, convertView, parent);
                                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                return v;
                            }

                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View v = super.getDropDownView(position, convertView, parent);
                                v.setBackgroundColor(Color.parseColor("#05000000"));
                                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                                return v;
                            }
                        };
                        spinnerTeerminalAdpter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                        binding.spinnerTerminal.setAdapter(spinnerTeerminalAdpter);

                    }
                });
            }
        });
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(this);
        binding.btnCreateeCase.setOnClickListener(this);
        binding.tvDone.setOnClickListener(this);
        binding.uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstKanthaFile = true;
                onImageSelected();
            }
        });
        binding.ReportsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstkantaParchiFile!=null)
                new PhotoFullPopupWindow(UploadDispladgeClass.this, R.layout.popup_photo_full, view, firstkantaParchiFile, null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAndClear(MyAddedDispladgeBagsListClass.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                startActivityAndClear(MyAddedDispladgeBagsListClass.class);
                break;
            case R.id.tv_done:
                startActivityAndClear(CaseListingActivity.class);
                break;
            case R.id.btn_createe_case:
                if (isValid()) {
                    String KanthaImage = "";
                    if (fileKantha != null) {
                        KanthaImage = "" + Utility.transferImageToBase64(fileKantha);
                    }
                    if (UserUniqueID == null) {
                        Toast.makeText(UploadDispladgeClass.this, getResources().getString(R.string.select_coustomer), Toast.LENGTH_LONG).show();
                    } else if (TerminalID == null) {
                        Toast.makeText(UploadDispladgeClass.this, getResources().getString(R.string.terminal_name), Toast.LENGTH_LONG).show();
                    } else if (commudityID == null) {
                        Toast.makeText(UploadDispladgeClass.this, getResources().getString(R.string.commudity_name), Toast.LENGTH_LONG).show();
                    } else if (stackID == null) {
                        Toast.makeText(UploadDispladgeClass.this, "Select Stack Number", Toast.LENGTH_LONG).show();
                    } else if (selectConvertOther == null) {
                        Toast.makeText(UploadDispladgeClass.this, getResources().getString(R.string.converted_by), Toast.LENGTH_LONG).show();
                    }  if (fileKantha == null) {
                        Toast.makeText(getApplicationContext(), "Upload to Displadge Image", Toast.LENGTH_LONG).show();
                    }else {
                        String finalKanthaImage = KanthaImage;
                        Utility.showDecisionDialog(UploadDispladgeClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                            @Override
                            public void callback() {
                                showDialog();
                                apiService.doAddDispladge(new CreateDispladgePostData(
                                        UserUniqueID, TerminalID, commudityID, stackID, stringFromView(binding.etCustomerWeight), stringFromView(binding.etCustomerBags), selectConvertOther,stringFromView(binding.notes), finalKanthaImage)).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
                                    @Override
                                    protected void onSuccess(LoginResponse body) {
                                        hideDialog();
                                        Utility.showAlertDialog(UploadDispladgeClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                                            @Override
                                            public void callback() {
                                                startActivityAndClear(MyAddedDispladgeBagsListClass.class);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CAMERA_PICTURE) {
                    if (this.camUri != null) {
                        if (firstKanthaFile) {
                            firstKanthaFile = false;
                            fileKantha = new File(compressImage(this.camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileKantha);
                            firstkantaParchiFile = String.valueOf(uri);
                            binding.ReportsImage.setImageURI(uri);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    boolean isValid() {
        if (TextUtils.isEmpty(stringFromView(binding.etCustomerWeight))) {
            return Utility.showEditTextError(binding.tilCustomerWeight, R.string.weight_kg);
        } else if (TextUtils.isEmpty(stringFromView(binding.etCustomerBags))) {
            return Utility.showEditTextError(binding.tilCustomerWeight, R.string.location);
        }else if (TextUtils.isEmpty(stringFromView(binding.notes))) {
            return Utility.showEditTextError(binding.notes, R.string.purpose);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
