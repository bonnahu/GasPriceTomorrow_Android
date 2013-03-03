/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.demo.notepad2;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Notepadv2 extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private NotesDbAdapter mDbHelper;
    private Cursor mNotesCursor;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();
        // n order for each list item in the ListView to register for the context menu
        // Registers a context menu to be shown for the given view 
        registerForContextMenu(getListView());
    }
    
    private void fillData() {
        // Get all of the rows from the database and create the item list
        mNotesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(mNotesCursor);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{NotesDbAdapter.KEY_TITLE};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
        	    new SimpleCursorAdapter(this, R.layout.notes_row, mNotesCursor, from, to);
        setListAdapter(notes);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID,0, R.string.menu_insert);
        menu.add(1, DELETE_ID, 1, R.string.menu_delete);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case INSERT_ID:
	            createNote();
	            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        // TODO: fill in rest of method
	}

    @Override
    // Now that the we've registered our ListView for a context menu and defined our context menu item,
    // we need to handle the callback when it is selected. For this, we need to identify
    // the list ID of the selected item, then delete it. 
	public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
        case DELETE_ID:
            // Here, we retrieve the AdapterContextMenuInfo with getMenuInfo().
        	// The id field of this object tells us the position of the item in the ListView. 
        	// We then pass this to the deleteNote() method of our NotesDbAdapter and the
        	// note is deleted. That's it for the context menu � notes can now be deleted.
        	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mDbHelper.deleteNote(info.id);
            fillData();
            return true;
        }
        return super.onContextItemSelected(item);
        // TODO: fill in rest of method
	}

    private void createNote() {
        // TODO: fill in implementation
    	Intent i = new Intent(this, NodeEdit.class);
    	// The startActivityForResult() method fires the Intent 
    	// in a way that causes a method in our Activity to be 
    	// called when the new Activity is completed. 
    	// The method in our Activity that receives the callback 
    	// is called onActivityResult() and we will implement 
    	// it in a later step.
    	// Here ACTIVITY_CREATE is the requestCode
    	startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    @Override
    // It is called when the user selects an item from the list
    // l: the ListView object it was invoked from
    // v: the View inside the ListView that was clicked on
    // position: the position in the list that was clicked
    // id: the mRowId of the item that was clicked.
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mNotesCursor;
        // Move to the proper position for the element that was selected in the list, 
        // with the moveToPosition() method.
        c.moveToPosition(position);
        Intent i = new Intent(this, NodeEdit.class);
        // putExtra() is the method to add items into the extras Bundle 
        // to pass in to intent invocations.
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        i.putExtra(NotesDbAdapter.KEY_TITLE, c.getString(
                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
        i.putExtra(NotesDbAdapter.KEY_BODY, c.getString(
                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        startActivityForResult(i, ACTIVITY_EDIT);
        // TODO: fill in rest of method
        
    }

    @Override
    // The above createNote() and onListItemClick() methods 
    // use an asynchronous Intent invocation. We need a 
    // handler for the callback, so here we fill in the body 
    // of the onActivityResult(). 
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	Bundle extras = intent.getExtras();

    	switch(requestCode) {
	    	case ACTIVITY_CREATE:
	    	    String title = extras.getString(NotesDbAdapter.KEY_TITLE);
	    	    String body = extras.getString(NotesDbAdapter.KEY_BODY);
	    	    mDbHelper.createNote(title, body);
	    	    fillData();
	    	    break;
	    	case ACTIVITY_EDIT:
	    	    Long mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
	    	    if (mRowId != null) {
	    	        String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
	    	        String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
	    	        mDbHelper.updateNote(mRowId, editTitle, editBody);
	    	    }
	    	    fillData();
	    	    break;
    	}
    }
}