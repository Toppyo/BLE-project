package com.panotech.ble_master_system;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylar on 2017/07/19.
 */

public class TestTableView extends TableLayout {

    protected int m_ColumnN=5;//列的数目。该值只能在构造函数中设置，设置之后不能修改。

    int m_LineColor=Color.BLACK;//线的颜色
    int m_LineWidth=1;//线宽

    protected List<TableRow> m_Rows;
    protected List<List<View>> m_Views;

    public int getM_ColumnN() {
        return m_ColumnN;
    }

    public TestTableView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        m_Rows=new ArrayList<TableRow>();
        m_Views=new ArrayList<List<View>>();
        this.setWillNotDraw(false);
    }
    public TestTableView(Context context,int n) {//指定列的数目
        super(context);
        // TODO Auto-generated constructor stub
        m_Rows=new ArrayList<TableRow>();
        m_Views=new ArrayList<List<View>>();
        if(n>0) m_ColumnN=n;
        else m_ColumnN=5;
        this.setWillNotDraw(false);
    }

    public void ClearRows()
    {
        if(m_Rows != null) m_Rows.clear();
        if(m_Views != null) m_Views.clear();
        m_Rows=new ArrayList<TableRow>();
        m_Views=new ArrayList<List<View>>();

        this.removeAllViews();
    }

    public int addRow(String[] st1, String[] st2)//添加一行，返回行数。如果objects的数目小于m_ColumnN则返回0。
    {
        if(st1==null) return 0;

        List<View> CRowViews=new ArrayList<View>();
        int i,nRows;
        TableRow CRow;
        View v1=null;
        View v2=null;

        m_Rows.add(new TableRow(this.getContext()));
        m_Views.add(new ArrayList<View>());
        nRows=m_Rows.size();
        CRowViews=m_Views.get(nRows-1);
        CRow=m_Rows.get(nRows-1);

        for(i=0;i<m_ColumnN;i++)
        {
            if(st2[i] != null) v1=createCellView(st1[i], st2[i]);
            if(v1 == null) v1=new View(getContext());
            v1.setPadding(5,5,5,5);
            CRow.addView(v1);
            CRowViews.add(v1);
            //for demo
            if(i == 1){
                v2 = createBlankView();
                CRow.addView(v2);
                CRowViews.add(v2);
            }
        }
        this.addView(CRow);

        return nRows;
    }

    public View GetCellView(int row,int column)//获得某一个单元格的View，row为行数，column为列数，从0开始
    {
        if(row<0||row>=m_Rows.size()) return null;
        else
        {
            if(column<0||column>=m_Views.get(row).size()) return null;
            else return m_Views.get(row).get(column);
        }
    }

    protected View createCellView(String st1, String st2)
    {
        View rView=null;
        TextView tView=new TextView(getContext());
        tView.setGravity(Gravity.CENTER);
        tView.setText(st1 + "\n" + st2);
//        tView.setAutoSizeTextTypeWithDefaults();
        rView=tView;
        return rView;
    }

    protected View createBlankView(){
        View rView = null;
        TextView textView = new TextView(getContext());
        textView.setText("      ");
        rView = textView;
        return rView;
    }

        /*
    public int AddRow(java.lang.Object objects[])//添加一行，返回行数。如果objects的数目小于m_ColumnN则返回0。
    {
        if(objects==null) return 0;
        if(objects.length<m_ColumnN) return 0;

        List<View> CRowViews=new ArrayList<View>();
        int i,nRows;
        TableRow CRow;
        String s1 = null,ss[]={" "};
        View v1=null;

        m_Rows.add(new TableRow(this.getContext()));
        m_Views.add(new ArrayList<View>());
        nRows=m_Rows.size();
        CRowViews=m_Views.get(nRows-1);
        CRow=m_Rows.get(nRows-1);

        for(i=0;i<m_ColumnN;i++)
        {
            if(objects[i] != null) v1=CreateCellView(objects[i]);
            if(v1 == null) v1=new View(getContext());
            CRow.addView(v1);
            CRowViews.add(v1);
        }
        this.addView(CRow);

        return nRows;
    }

    protected View CreateCellView(Object obj)//根据obj的类型创建一个VIEW并返回之，如果无法识别Object的类型返回null
    {
        View rView=null;
        String classname = obj.getClass().toString();

        switch (classname)
        {
            case "class java.lang.String"://这个值是String.class.toString()的结果

                TextView tView=new TextView(getContext());
                tView.setText((String) obj);
                rView=tView;
                break;

            case "class android.graphics.Bitmap":
                ImageView iView=new ImageView(getContext());
                iView.setImageBitmap((Bitmap) obj);
                rView=iView;
                break;

            //在此处识别其它的类型，创建一个View并附给rView

            default:
                rView=null;
                break;
        }
        return rView;
    }

   @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //
        if(m_Rows.size()<1) return;

        Paint paint1=new Paint();
        int i,nRLinePosition=0,nCLinePosition=0,width=getWidth(),height=getHeight();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(m_LineWidth);
        paint1.setColor(m_LineColor);

        canvas.drawRect(new Rect(1, 1, width, height), paint1);

        for(i=0;i<m_Rows.size();i++)
        {
            nRLinePosition+=m_Rows.get(i).getHeight();
            canvas.drawLine(0, nRLinePosition, width, nRLinePosition, paint1);
        }
        for(i=0;i<m_Views.get(0).size();i++)
        {
            nCLinePosition+=m_Views.get(0).get(i).getWidth();
            canvas.drawLine(nCLinePosition, 0, nCLinePosition, height, paint1);
        }
    }
    */
}
