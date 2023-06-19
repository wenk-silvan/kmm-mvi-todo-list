//
//  TodoListScreen.swift
//  iosApp
//
//  Created by Wenk Silvan on 30.05.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TodoListScreen<Destination: View>: View {
    
    var destinationProvider: () -> Destination
    
    private var store: TodoListStore
    
    @StateObject private var state = AppState(store: nil)
    
    init(store: TodoListStore, destinationProvider: @escaping () -> Destination) {
        self.store = store
        self.destinationProvider = destinationProvider
    }
    
    var body: some View {
        
        let textBinding = Binding<String>(
            get: { self.state.todoListState?.newItem ?? "" },
            set: { store.dispatch(action: TodoListAction.UpdateNewItem(text: $0)); }
        )
        
        VStack {
            NavigationLink(
                destination: destinationProvider(),
                isActive: $state.isItemSelected
            ) { EmptyView() }.hidden()
            Text("Todolist").font(.title2)
            HStack {
                TextField("Add something...", text: textBinding)
                    .font(.body)
                    .textInputAutocapitalization(.never)
                    .disableAutocorrection(true)
                    .alert(isPresented: $state.showError) {
                        Alert(title: Text("Error"), message: Text("Input is not valid"), dismissButton: .default(Text("Ok")))
                    }
                Button(action: onClickAddItem) {
                    Image(systemName: "plus").foregroundColor(.black)
                }
            }
            if (state.todoListState?.todoList != nil) { todoListView }
            Spacer()
        }
        .padding()
        .onAppear {
            self.state.setStore(store: store)
        }
    }
    
    func onClickAddItem() {
        store.dispatch(action: TodoListAction.Add(text: state.todoListState!.newItem))
    }
    
    var todoListView: some View {
        List {
            ForEach(state.todoListState!.todoList, id: \.self.created) { item in
                todoListItemView(item: item)
            }.onDelete { indexSet in onDeleteItems(indexSet: indexSet)}
        }
        .padding(.top)
        .listStyle(.plain)
        .listRowSeparator(.hidden)
    }
    
    func onDeleteItems(indexSet: IndexSet) {
        indexSet.forEach { index in
            let created = state.todoListState!.todoList[index].created
            store.dispatch(action: TodoListAction.Remove(dateTime: created))
        }
    }
    
    func todoListItemView(item: TodoItem) -> some View {
        return Button(action: { onClickItem(item: item) }) {
            HStack {
                Text(item.text)
                Spacer()
                Text(DateTimeUtil().formatNoteDate(dateTime: item.created)).font(.footnote).fontWeight(.light)
            }
        }
    }
    
    func onClickItem(item: TodoItem) {
        store.dispatch(action: TodoListAction.ShowItemDetail(item: item))
    }
}

struct TodoListScreen_Previews: PreviewProvider {
    static var previews: some View {
        Text("To be done")
    }
}
