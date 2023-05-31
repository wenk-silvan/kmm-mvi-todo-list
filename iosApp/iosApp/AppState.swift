//
//  AppState.swift
//  iosApp
//
//  Created by Wenk Silvan on 30.05.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

class AppState: ObservableObject {
    
    @Published var todoListState : TodoListState?
    
    @Published var isItemSelected = false

    init(store: TodoListStore?) {
        if (store != nil) {
            setStore(store: store!)
        }
   }
    
    func setStore(store: TodoListStore) {
        store.onStateChange { newState in
            self.todoListState = newState
        }
        store.onSideEffectEmitted { sideEffect in
            if (sideEffect is TodoListSideEffect.Error) {
                print("Error")
            }
            else if (sideEffect is TodoListSideEffect.NavigateToItemListScreen) {
                self.isItemSelected = false
            }
            else if (sideEffect is TodoListSideEffect.NavigateToItemDetailScreen) {
                self.isItemSelected = true
            }
        }
    }
}

