import {MenuItem, Select} from '@mui/material';
import {categoryMenuProps} from './const/headerConst';
import {useCategory} from './hooks/useCategory';

const CategorySelect = () => {
  const {category, categoryList, handleSelect} = useCategory();

  return (
    <Select
      size="small"
      sx={{minWidth: 150, fontWeight: 600}}
      value={categoryList.includes(category) ? category : ''}
      name="category"
      onChange={handleSelect}
      displayEmpty
      MenuProps={categoryMenuProps}
      inputProps={{'aria-label': 'categories'}}>
      <MenuItem value="">All Categories</MenuItem>
      {categoryList.map(categoryElement => (
        <MenuItem key={categoryElement} value={categoryElement}>
          {categoryElement}
        </MenuItem>
      ))}
    </Select>
  );
};

export default CategorySelect;
