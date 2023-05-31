//
//  TodoItemScreen.swift
//  iosApp
//
//  Created by Wenk Silvan on 30.05.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TodoItemScreen: View {
    private var store: TodoListStore
    
    @StateObject private var state = AppState(store: nil)
    
    init(store: TodoListStore) {
        self.store = store
    }

    var body: some View {
        
        let textBinding = Binding<String>(
            get: { self.state.todoListState?.editItem.text ?? "" },
            set: { store.dispatch(action: TodoListAction.UpdateEditItemText(text: $0)); }
        )
        
        VStack {
            Text("Item Details").font(.title2)
            state.todoListState != nil
                ? Text(DateTimeUtil().formatNoteDate(dateTime: state.todoListState!.editItem.created)).font(.footnote)
                : Text("")
            TextField("", text: textBinding)
                .font(.body)
                .textInputAutocapitalization(.never)
                .disableAutocorrection(true)
            Button(action: onSaveItem) {
                Text("Save")
            }
            Spacer()
        }
        .padding()
        .onAppear {
            self.state.setStore(store: store)
        }
    }
    
    func onSaveItem() {
        store.dispatch(action: TodoListAction.SaveEditItem())
    }
}

struct TodoItemScreen_Previews: PreviewProvider {
    static var previews: some View {
        Text("To be done")
    }
}
