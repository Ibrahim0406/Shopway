import React from 'react';
import { ArrayInput, BooleanInput, Edit, ImageField, ImageInput, NumberInput, required, SelectInput, SimpleForm, SimpleFormIterator, TextInput } from 'react-admin'

function EditProduct() {
    return (
        <Edit>
            <SimpleForm>
                <TextInput label="Name" source='name'/>
                <TextInput label="Description" source='description'/>
                <TextInput label="Price" source='price' type='number'/>
                <TextInput label="Brand" source='brand'/>
            </SimpleForm>
        </Edit>
    );
}

export default EditProduct;