function TopListItem ({stats}) {
    if(stats.country === stats.continent)
        return;
    
    return ( 
        <tr>
            <td>{stats.country}</td>
            <td>{stats.newCases}</td>
            <td>{stats.newDeaths}</td>
        </tr>
    );
}
 
export default TopListItem;