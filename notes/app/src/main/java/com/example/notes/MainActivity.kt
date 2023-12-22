package com.example.notes

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var listNotes= ArrayList<Note>()
    var rlist:ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        rlist = findViewById<ListView>(R.id.lvnotes)
        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    @SuppressLint("Range")
    fun LoadQuery(title:String){
        var dbManager=DBManager(this)
        val projection = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projection,"Title Like ?",selectionArgs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID,Title,Description))
            }while(cursor.moveToNext())
        }
        var myNotesAdapter = MyNotesAdapter(this,listNotes)
        rlist!!.adapter = myNotesAdapter
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)

        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String):Boolean{
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                LoadQuery("%"+query+"%")
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item != null){
            when(item.itemId){
                R.id.addNote -> {
                    var intent = Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    open inner class MyNotesAdapter:BaseAdapter{
        var context:Context? = null
        var listofnoteAdapter = ArrayList<Note>()
        constructor(context: Context,listofnote:ArrayList<Note>):super(){
            this.listofnoteAdapter = listofnote
            this.context = context
        }
        override fun getCount(): Int {
            return listofnoteAdapter.size
        }

        override fun getItem(p0: Int): Any {
            return listofnoteAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        @SuppressLint("MissingInflatedId")
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listofnoteAdapter[p0]
            myView.findViewById<TextView>(R.id.tvTitle).text = myNote.nodeName
            myView.findViewById<TextView>(R.id.tvDes).text = myNote.nodeDes
            myView.findViewById<ImageButton>(R.id.ibDelete).setOnClickListener(View.OnClickListener {
                var dbManager = DBManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
            })
            myView.findViewById<ImageButton>(R.id.ibEdit).setOnClickListener(View.OnClickListener {
                GoToUpdate(myNote)
            })
            myView.findViewById<LinearLayout>(R.id.llnote).setOnClickListener{
                var intent = Intent(myView.context,activity_noteinfo::class.java)
                intent.putExtra("name",myNote.nodeName)
                intent.putExtra("des",myNote.nodeDes)
                myView.context!!.startActivity(intent)
            }
            return myView
        }
        fun GoToUpdate(note:Note){
            var intent = Intent(this@MainActivity,AddNotes::class.java)
            intent.putExtra("ID",note.nodeID)
            intent.putExtra("name",note.nodeName)
            intent.putExtra("des",note.nodeDes)
            startActivity(intent)
        }
    }
}