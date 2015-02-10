package com.fortysevendeg.android.scaladays.ui.commons

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class EmptyAdapter extends RecyclerView.Adapter[RecyclerView.ViewHolder] {
  
  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder = throw new IllegalStateException

  override def getItemCount: Int = 0

  override def onBindViewHolder(vh: RecyclerView.ViewHolder, i: Int): Unit = throw new IllegalStateException
}