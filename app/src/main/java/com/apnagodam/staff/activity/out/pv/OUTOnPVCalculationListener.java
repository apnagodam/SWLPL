package com.apnagodam.staff.activity.out.pv;


public interface OUTOnPVCalculationListener {
  void onAdd(int position);
  void onRemove(int position);
  void onTextWeight(int position,double dhang,double danda,double height,double plusminus,String remark);
}
