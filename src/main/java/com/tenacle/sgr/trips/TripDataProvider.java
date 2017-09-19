/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.trips;

import org.vaadin.addon.calendar.item.BasicItemProvider;

/**
 *
 * @author samuel
 */
public class TripDataProvider extends BasicItemProvider<TripItem> {

    void removeAllEvents() {
        this.itemList.clear();
        fireItemSetChanged();
    }
}
