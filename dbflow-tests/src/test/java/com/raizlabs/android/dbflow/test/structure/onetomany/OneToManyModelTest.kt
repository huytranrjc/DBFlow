package com.raizlabs.android.dbflow.test.structure.onetomany

import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import com.raizlabs.android.dbflow.test.FlowTestCase
import com.raizlabs.android.dbflow.test.structure.TestModel2
import org.junit.Assert.*
import org.junit.Test

/**
 * Description: Tests the [OneToMany] annotation to ensure it works as expected.
 */
class OneToManyModelTest : FlowTestCase() {

    @Test
    fun testOneToManyModel() {
        Delete.tables(TestModel2::class.java, OneToManyModel::class.java)

        var testModel2 = TestModel2()
        testModel2.name = "Greater"
        testModel2.order = 4
        testModel2.save()

        testModel2 = TestModel2()
        testModel2.name = "Lesser"
        testModel2.order = 1
        testModel2.save()

        // assert we save
        var oneToManyModel = OneToManyModel()
        oneToManyModel.name = "HasOrders"
        oneToManyModel.save()
        assertTrue(oneToManyModel.exists())

        // assert loading works as expected.
        oneToManyModel = Select().from(OneToManyModel::class.java).querySingle()!!
        assertNotNull(oneToManyModel.getRelatedOrders())
        assertTrue(!oneToManyModel.getRelatedOrders().isEmpty())

        // assert the deletion cleared the variable
        oneToManyModel.delete()
        assertFalse(oneToManyModel.exists())
        assertNull(oneToManyModel.orders)

        // assert singular relationship was deleted.
        val list = Select().from(TestModel2::class.java).queryList()
        assertTrue(list.size == 1)

        Delete.tables(TestModel2::class.java, OneToManyModel::class.java)
    }

}
